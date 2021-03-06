package com.simi.action.app.order;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import com.simi.action.app.JUnitActionBase;

public class TestOrderAlipayController extends JUnitActionBase  {

	/**
	 * 		// 7. 订单在线支付成功同步接口
	 *     ​http://localhost:8080/onecare/app/order/online_pay_notify.json
	 *     http://182.92.160.194/trac/wiki/%E8%AE%A2%E5%8D%95%E6%8F%90%E4%BA%A4%E6%8E%A5%E5%8F%A3
	 *     POST
	 */
	@Test
    public void testOnlinePay() throws Exception {
		String url = "/app/order/online_pay_notify.json";

     	MockHttpServletRequestBuilder postRequest = post(url);
	    postRequest = postRequest.param("mobile", "");
	    postRequest = postRequest.param("order_no", "713325602299445248");
	    postRequest = postRequest.param("pay_type", "1");
	    postRequest = postRequest.param("notify_id", "3e9689bbb883910b5471ec498204d01ipa");
	    postRequest = postRequest.param("notify_time", "2016-06-24 19:24:49");
	    postRequest = postRequest.param("trade_no", "2016032521001004350203142264");
	    postRequest = postRequest.param("trade_status", "TRADE_FINISHED");
	    postRequest = postRequest.param("pay_account", "15727372986");
	    
	    
	    ResultActions resultActions = mockMvc.perform(postRequest);

	    resultActions.andExpect(content().contentType(this.mediaType));
	    resultActions.andExpect(status().isOk());


	    System.out.println("RestultActons: " + resultActions.andReturn().getResponse().getContentAsString());
	
	    Thread.sleep(200000); // 因为junit结束会结束jvm，所以让它等会异步线程  
	}
}
