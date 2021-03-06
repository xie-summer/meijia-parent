package com.meijia.utils.huanxin;

import org.apache.commons.lang3.StringUtils;
import org.glassfish.jersey.client.JerseyWebTarget;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.meijia.utils.huanxin.comm.Constants;
import com.meijia.utils.huanxin.comm.HTTPMethod;
import com.meijia.utils.huanxin.comm.Roles;
import com.meijia.utils.huanxin.utils.JerseyUtils;
import com.meijia.utils.huanxin.vo.ClientSecretCredential;
import com.meijia.utils.huanxin.vo.Credential;
import com.meijia.utils.huanxin.vo.EndPoints;

/**
 * REST API Demo: 发送消息 REST API Jersey2.9实现
 * 
 * Doc URL: http://www.easemob.com/docs/rest/sendmessage/
 * 
 * @author Lynch 2014-07-12
 * 
 */
public class EasemobMessages {

	private static Logger LOGGER = LoggerFactory.getLogger(EasemobMessages.class);
	private static final String APPKEY = Constants.APPKEY;
	private static JsonNodeFactory factory = new JsonNodeFactory(false);

    // 通过app的client_id和client_secret来获取app管理员token
    private static Credential credential = new ClientSecretCredential(Constants.APP_CLIENT_ID,
            Constants.APP_CLIENT_SECRET, Roles.USER_ROLE_APPADMIN);

    public static void main(String[] args) {
        //  检测用户是否在线
        String targetuserPrimaryKey = "simi-sec-1";
        ObjectNode usernode = getUserStatus(targetuserPrimaryKey);
        if (null != usernode) {
            LOGGER.info("检测用户是否在线: " + usernode.toString());
        }

        // 给用户发一条文本消息
        String from = "simi-sec-2";
        String targetTypeus = "users";
        ObjectNode ext = factory.objectNode();
        ArrayNode targetusers = factory.arrayNode();
        targetusers.add("simi-sec-1");
        targetusers.add("simi-user-92");
        ObjectNode txtmsg = factory.objectNode();
        txtmsg.put("msg", "Hello simi!");
        txtmsg.put("type","txt");
        ObjectNode sendTxtMessageusernode = sendMessages(targetTypeus, targetusers, txtmsg, from, ext);
        if (null != sendTxtMessageusernode) {
            LOGGER.info("给用户发一条文本消息: " + sendTxtMessageusernode.toString());
        }

        // 给一个群组发文本消息
//        String targetTypegr = "chatgroups";
//        ArrayNode  chatgroupidsNode = (ArrayNode) EasemobChatGroups.getAllChatgroupids().path("data");
//        ArrayNode targetgroup = factory.arrayNode();
//        targetgroup.add(chatgroupidsNode.get(0).path("groupid").asText());
//        ObjectNode sendTxtMessagegroupnode = sendMessages(targetTypegr, targetgroup, txtmsg, from, ext);
//        if (null != sendTxtMessagegroupnode) {
//            LOGGER.info("给一个群组发文本消息: " + sendTxtMessagegroupnode.toString());
//        }

        // 给用户发一条图片消息
//        File uploadImgFile = new File("/home/lynch/Pictures/24849.jpg");
//        ObjectNode imgDataNode = EasemobFiles.mediaUpload(uploadImgFile);
//        String imgFileUUID = imgDataNode.path("entities").get(0).path("uuid").asText();
//        String shareSecret = imgDataNode.path("entities").get(0).path("share-secret").asText();
//        if (null != imgDataNode) {
//            LOGGER.info("上传图片文件: " + imgDataNode.toString());
//        }
//        ObjectNode imgmsg = factory.objectNode();
//        imgmsg.put("type","img");
//        imgmsg.put("url",  EndPoints.CHATFILES_TARGET.resolveTemplate("org_name", APPKEY.split("#")[0]).resolveTemplate("app_name",
//                APPKEY.split("#")[1]).getUri().toString() + imgFileUUID);
//        imgmsg.put("filename", "24849.jpg");
//        imgmsg.put("length", 10);
//        imgmsg.put("secret", shareSecret);
//        ObjectNode sendimgMessageusernode = sendMessages(targetTypeus, targetusers, imgmsg, from, ext);
//        if (null != sendimgMessageusernode) {
//            LOGGER.info("给一个群组发文本消息: " + sendimgMessageusernode.toString());
//        }

        // 给一个群组发图片消息
//        ObjectNode sendimgMessagegroupnode = sendMessages(targetTypegr, targetgroup, imgmsg, from, ext);
//        if (null != sendimgMessagegroupnode) {
//            LOGGER.info("给一个群组发文本消息: " + sendimgMessagegroupnode.toString());
//        }

        // 给用户发一条语音消息
//        File uploadAudioFile = new File("/home/lynch/Music/music.MP3");
//        ObjectNode audioDataNode = EasemobFiles.mediaUpload(uploadAudioFile);
//        String audioFileUUID = audioDataNode.path("entities").get(0).path("uuid").asText();
//        String audioFileShareSecret = audioDataNode.path("entities").get(0).path("share-secret").asText();
//        if (null != audioDataNode) {
//            LOGGER.info("上传语音文件: " + audioDataNode.toString());
//        }
//        ObjectNode audiomsg = factory.objectNode();
//        audiomsg.put("type","audio");
//        audiomsg.put("url", EndPoints.CHATFILES_TARGET.resolveTemplate("org_name", APPKEY.split("#")[0]).resolveTemplate("app_name",
//                APPKEY.split("#")[1]).getUri().toString() + audioFileUUID);
//        audiomsg.put("filename", "music.MP3");
//        audiomsg.put("length", 10);
//        audiomsg.put("secret", audioFileShareSecret);
//        ObjectNode sendaudioMessageusernode = sendMessages(targetTypeus, targetusers, audiomsg, from, ext);
//        if (null != sendaudioMessageusernode) {
//            LOGGER.info("给用户发一条语音消息: " + sendaudioMessageusernode.toString());
//        }

        // 给一个群组发语音消息
//        ObjectNode sendaudioMessagegroupnode = sendMessages(targetTypegr, targetgroup, audiomsg, from, ext);
//        if (null != sendaudioMessagegroupnode) {
//            LOGGER.info("给一个群组发语音消息: " + sendaudioMessagegroupnode.toString());
//        }

        // 给用户发一条透传消息
        ObjectNode cmdmsg = factory.objectNode();
        cmdmsg.put("action", "order");
        cmdmsg.put("type","cmd");
        
//        ext.put("order_id", 1);
//        ext.put("order_no", "611081901792296960");
//        ext.put("order_pay_type", 0);
//        ext.put("add_time", "2015-06-18 13:34:33");
//        ext.put("service_type_name", "通用");
//        ext.put("service_content", "下午2点叫快递");
////        ext.put("service_time", "2015-06-18 14:00:00");
////        ext.put("service_addr", "北京东直门外大街42号宇飞大厦612");
//        ext.put("service_time", "");
//        ext.put("service_addr", "");
//        ext.put("order_money", "10");

      ext.put("order_id", 1);
      ext.put("order_no", "611081901792296960");
      ext.put("order_pay_type", 1);
      ext.put("add_time", "2015-06-18 13:34:33");
      ext.put("service_type_name", "定机票");
      ext.put("service_content", "2015-06-20 13:25 北京到上海, 航班号 CA8802, T3航站楼起飞");
//      ext.put("service_time", "2015-06-18 14:00:00");
//      ext.put("service_addr", "北京东直门外大街42号宇飞大厦612");
      ext.put("service_time", "");
      ext.put("service_addr", "");
      ext.put("order_money", "560");        
        
        ObjectNode sendcmdMessageusernode = sendMessages(targetTypeus, targetusers, cmdmsg, from, ext);
        if (null != sendcmdMessageusernode) {
            LOGGER.info("给用户发一条透传消息: " + sendcmdMessageusernode.toString());
        }

    }

    /**
	 * 检测用户是否在线
	 * 
	 * @param targetUserPrimaryKey
	 * @return
	 */
	public static ObjectNode getUserStatus(String targetUserPrimaryKey) {

		ObjectNode objectNode = factory.objectNode();

		// check appKey format
		if (!JerseyUtils.match("^(?!-)[0-9a-zA-Z\\-]+#[0-9a-zA-Z]+", APPKEY)) {
			LOGGER.error("Bad format of Appkey: " + APPKEY);

			objectNode.put("message", "Bad format of Appkey");

			return objectNode;
		}

		// check properties that must be provided
		if (StringUtils.isEmpty(targetUserPrimaryKey)) {
			LOGGER.error("You must provided a targetUserPrimaryKey .");

			objectNode.put("message", "You must provided a targetUserPrimaryKey .");

			return objectNode;
		}

		try {

			JerseyWebTarget webTarget = EndPoints.USERS_TARGET.resolveTemplate("org_name", APPKEY.split("#")[0])
					.resolveTemplate("app_name", APPKEY.split("#")[1]).path(targetUserPrimaryKey).path("status");

			objectNode = JerseyUtils.sendRequest(webTarget, null, credential, HTTPMethod.METHOD_GET, null);

			String userStatus = objectNode.get("data").path(targetUserPrimaryKey).asText();
			if ("online".equals(userStatus)) {
				LOGGER.error(String.format("The status of user[%s] is : [%s] .", targetUserPrimaryKey, userStatus));
			} else if ("offline".equals(userStatus)) {
				LOGGER.error(String.format("The status of user[%s] is : [%s] .", targetUserPrimaryKey, userStatus));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return objectNode;
	}
	
	/**
	 * 发送文本消息接口
	 */
	
	public static Boolean sendMessageTxt(String from, String to, String msg) {
		
		 // 给用户发一条文本消息
        String targetTypeus = "users";
        ObjectNode ext = factory.objectNode();
        ArrayNode targetusers = factory.arrayNode();
        targetusers.add(to);

        ObjectNode txtmsg = factory.objectNode();
        txtmsg.put("msg", msg);
        txtmsg.put("type","txt");
        ObjectNode sendTxtMessageusernode = sendMessages(targetTypeus, targetusers, txtmsg, from, ext);
        if (null != sendTxtMessageusernode) {
            LOGGER.info("给用户发一条文本消息: " + sendTxtMessageusernode.toString());
        }
		
		return true;
	}
	
	/**
	 * 发送消息
	 * 
	 * @param targetType
	 *            消息投递者类型：users 用户, chatgroups 群组
	 * @param target
	 *            接收者ID 必须是数组,数组元素为用户ID或者群组ID
	 * @param msg
	 *            消息内容
	 * @param from
	 *            发送者
	 * @param ext
	 *            扩展字段
	 * 
	 * @return 请求响应
	 */
	public static ObjectNode sendMessages(String targetType, ArrayNode target, ObjectNode msg, String from,
			ObjectNode ext) {

		ObjectNode objectNode = factory.objectNode();

		ObjectNode dataNode = factory.objectNode();

		// check appKey format
		if (!JerseyUtils.match("^(?!-)[0-9a-zA-Z\\-]+#[0-9a-zA-Z]+", APPKEY)) {
			LOGGER.error("Bad format of Appkey: " + APPKEY);

			objectNode.put("message", "Bad format of Appkey");

			return objectNode;
		}

		// check properties that must be provided
		if (!("users".equals(targetType) || "chatgroups".equals(targetType))) {
			LOGGER.error("TargetType must be users or chatgroups .");

			objectNode.put("message", "TargetType must be users or chatgroups .");

			return objectNode;
		}

		try {
			// 构造消息体
			dataNode.put("target_type", targetType);
			dataNode.put("target", target);
			dataNode.put("msg", msg);
			dataNode.put("from", from);
			dataNode.put("ext", ext);

			JerseyWebTarget webTarget = EndPoints.MESSAGES_TARGET.resolveTemplate("org_name", APPKEY.split("#")[0]).resolveTemplate(
					"app_name", APPKEY.split("#")[1]);

			objectNode = JerseyUtils.sendRequest(webTarget, dataNode, credential, HTTPMethod.METHOD_POST, null);

			objectNode = (ObjectNode) objectNode.get("data");
			for (int i = 0; i < target.size(); i++) {
				String resultStr = objectNode.path(target.path(i).asText()).asText();
				if ("success".equals(resultStr)) {
					LOGGER.error(String.format("Message has been send to user[%s] successfully .", target.path(i).asText()));
				} else if (!"success".equals(resultStr)) {
					LOGGER.error(String.format("Message has been send to user[%s] failed .", target.path(i).asText()));
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return objectNode;
	}
}
