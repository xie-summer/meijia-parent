package com.simi.action.app.user;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.meijia.utils.MeijiaUtil;
import com.meijia.utils.TimeStampUtil;
import com.simi.action.app.BaseController;
import com.simi.common.ConstantMsg;
import com.simi.common.Constants;
import com.simi.po.model.dict.DictCardType;
import com.simi.po.model.order.OrderCards;
import com.simi.po.model.user.Users;
import com.simi.service.async.NoticeSmsAsyncService;
import com.simi.service.dict.CardTypeService;
import com.simi.service.order.OrderCardsService;
import com.simi.service.user.UsersService;
import com.simi.vo.AppResultData;

@Controller
@RequestMapping(value = "/app/user")
public class UserCardController extends BaseController {

	@Autowired
	private UsersService usersService;
	
	@Autowired
	private OrderCardsService orderCardsService;
	
	@Autowired
	private CardTypeService cardTypeService;
	
	@Autowired
	private NoticeSmsAsyncService noticeSmsAsyncService;


	// 8. 会员充值接口
	/**
	 * userId true long 用户Id
	 * card_type true int 充值卡类型
	 * pay_type true int 支付类型 0 =
	 * 余额支付 1 = 支付宝 2 = 微信支付 3 = 智慧支付 4 = 上门刷卡（保留，站位）
	 */
	@RequestMapping(value = "card_buy", method = RequestMethod.POST)
	public AppResultData<Object> cardBuy(
			@RequestParam("user_id")	Long userId,
			@RequestParam("card_type") Long cardType,
			@RequestParam("pay_type")  Short payType,
			@RequestParam(value = "card_money", required = false, defaultValue="0") BigDecimal cardMoney) {
//	    操作表 order_cards
//	    根据card_type 传递参数从表 dict_card_type 获取相应的金额

		AppResultData<Object> result = new AppResultData<Object>( Constants.SUCCESS_0, ConstantMsg.SUCCESS_0_MSG, "");		
		
		//暂时不能进行充值功能：
//		result.setStatus(Constants.ERROR_999);
//		result.setMsg("系统维护,暂时无法充值.");
//		return result;
		
		
		Users users = usersService.selectByPrimaryKey(userId);
		
		// 判断是否为注册用户，非注册用户返回 999
		if (users == null) {
			result.setStatus(Constants.ERROR_999);
			result.setMsg(ConstantMsg.USER_NOT_EXIST_MG);
			return result;
		}		
		
		if (cardType.equals(0L)) {
			if (cardMoney == null || cardMoney.equals(0)) {
				result.setStatus(Constants.ERROR_999);
				result.setMsg("充值金额不正确!");
				return result;
			}
			
			OrderCards record = orderCardsService.initCardMoney(users, cardType, cardMoney, payType);
			orderCardsService.insert(record);
			result.setData(record);
			return result;
		}
		
		
		DictCardType dictCardType = cardTypeService.selectByPrimaryKey(cardType);

		OrderCards record = orderCardsService.initOrderCards(users, cardType, dictCardType, payType);
		orderCardsService.insert(record);

		result.setData(record);
		return result;
	}
	
	// 8. 会员充值在线支付成功同步接口
		/**
		 * 会员充值在线支付成功同步接口
		 * @param mobile  手机号
		 * @param card_order_no  订单号
		 * @param pay_type 支付类型 0 = 余额支付 1 = 支付宝 2 = 微信支付 3 = 智慧支付 4 = 上门刷卡（保留，站位）
		 * @param notify_id  通知ID
		 * @param notify_time 通知时间
		 * @param trade_no 流水号
		 * @param trade_status true 支付状态
		 * 											 支付宝客户端成功状态为: TRADE_FINISHED 或者为 TRADE_SUCCESS
		 *                                           支付宝网页版成功状态为: success
		 *                                           微信支付成功的状态为: SUCCESS
		 *
		 * 1. 判断必选参数
		 * 2. 根据card_order_no,从order_cards找出对应的订单
		 * 3. 判断该订单是否已经支付完成
		 * 1) 判断字段order_status = 1
		 * 4. 如果该订单为未支付的状态，则需要做如下的操作
		 * 1) 操作表user_pay_status ，插入一条新的记录，记录支付的信息
		 * 2) 用户的消费明细记录，操作表为user_detail_pay
		 * 3) 将card_orders 表的状态改变为 order_status = 2 ,已支付状态, 支付状态pay_type = 1 支付宝支付
		 * 4) 用户余额，扣除相应的金额，注意如果有优惠卷的金额，操作表为users
		 * 注意以上4个步骤必须为同一个事务	 *
		 */
		@RequestMapping(value = "card_online_pay", method = RequestMethod.POST)
		public AppResultData<Object> cardOnlinePay(
			@RequestParam(value = "user_id", defaultValue = "0") Long userId, 
			@RequestParam("card_order_no") 	String cardOrderNo,
			@RequestParam("pay_type") 			Short payType,
			@RequestParam("notify_id") 			String notifyId,
			@RequestParam("notify_time") 		String notifyTime,
			@RequestParam("trade_no") 			String tradeNo,
			@RequestParam("trade_status") 		String tradeStatus,
			@RequestParam(value = "pay_account", required = false, defaultValue="") String payAccount) {

			AppResultData<Object> result = new AppResultData<Object>( Constants.SUCCESS_0, ConstantMsg.SUCCESS_0_MSG,"");

			//判断如果不是正确支付状态，则直接返回.
			Boolean paySuccess = MeijiaUtil.isPaySuccess(tradeStatus);
			if (paySuccess == false )  {
				return result;
			}

			OrderCards orderCards = orderCardsService.selectByOrderCardsNo(cardOrderNo);
			Long updateTime = TimeStampUtil.getNow() / 1000;

			userId = orderCards.getUserId();
			

			//如果已经付款，则直接返回
			if (orderCards != null && orderCards.getOrderStatus().equals(Constants.PAY_STATUS_1)) {
				return result;
			}
			
			orderCards.setPayType(payType);
			orderCards.setOrderStatus(Constants.PAY_STATUS_1);
			orderCards.setUpdateTime(updateTime);
			// 更新orders,orderPrices,Users,插入消费明细UserDetailPay
			orderCardsService.updateOrderByOnlinePay(orderCards, tradeNo, tradeStatus, payAccount);
			
			//通知运营人员，用户充值成功
			noticeSmsAsyncService.noticeOrderCardUser(orderCards.getId());
			
			return result;

		}
}
