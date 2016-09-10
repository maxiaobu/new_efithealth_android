/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.efithealth.app;

import android.os.Environment;

public class Constant {

	// 环信 message 标识
	public static final String NEW_FRIENDS_USERNAME = "item_new_friends";
	public static final String GROUP_USERNAME = "item_groups";
	public static final String CHAT_ROOM = "item_chatroom";
	public static final String MESSAGE_ATTR_IS_VOICE_CALL = "is_voice_call";
	public static final String MESSAGE_ATTR_IS_VIDEO_CALL = "is_video_call";
	public static final String ACCOUNT_REMOVED = "account_removed";
	public static final String CHAT_ROBOT = "item_robots";
	public static final String MESSAGE_ATTR_ROBOT_MSGTYPE = "msgtype";

	public static final String DIR_PATH = Environment
			.getExternalStorageDirectory() + "/efithealth/";

	public static final String URL_MASSAGE = "http://192.168.1.182:8080/efithealth/";
	public static final String URL_BASE = URL_MASSAGE;//"http://123.56.195.32:8080/efithealth/";
	public static final String URL_GROUP = URL_MASSAGE+"mgroup!";//"http://192.168.1.185:18080/efithealth/mgroup!";
	// public static final String URL_BASE =
	// "http://192.168.1.137:18080/efithealth/";

	public static final String URL_RESOURCE = "http://efithealthresource.oss-cn-beijing.aliyuncs.com";
	public static final String URL_LOGIN = URL_BASE + "mlogin.do";
	public static final String URL_SENDCODE = URL_BASE + "msendCode.do";
	public static final String URL_SENDCODE_FORGET = URL_BASE + "mrsendCode.do";
	public static final String URL_SENDCODE_CHECK = URL_BASE + "mcheckCode.do";
	public static final String URL_SENDCODE_CHECK_FORGET = URL_BASE
			+ "mrcheckCode.do";
	public static final String URL_RESETPASSWORD = URL_BASE
			+ "mresetMempass.do";
	public static final String URL_REGISTER = URL_BASE + "mregister.do";
	public static final String URL_HOME = URL_BASE + "mindex.do";
	public static final String URL_IMGWALL_UPLOAD = URL_BASE
			+ "mphotowallsave.do";
	public static final String URL_IMGWALL = URL_BASE + "mphotowall.do";
	public static final String URL_MYINFO = URL_BASE + "mtoupdateMember.do";
	public static final String URL_MYINFO_UPDATE = URL_BASE
			+ "mupdateMember.do";
	public static final String URL_PUBLISH = URL_BASE + "mdynamicsave.do";
	public static final String URL_ME = URL_BASE + "mme.do";
	public static final String URL_SIGN = URL_BASE + "msingin.do";// 判断签到
	public static final String URL_SIGNIN = URL_BASE + "msinginsave.do";// 签到
	public static final String URL_MCOURSELIST = URL_BASE + "mcourseList.do";// 课程管理->上线课程
	public static final String URL_ISSUE_COURSE = URL_BASE + "mpcoursesave.do";// 课程管理->发布课程
	public static final String URL_CLUB_MESSAGE = URL_BASE + "mbindList.do";// 课程管理->获得教练绑定的俱乐部信息
	public static final String URL_UPDATECOURSE = URL_BASE + "mupdateCourse.do";// 课程修改
	public static final String URL_DELETECOURSE = URL_BASE + "mdeleteCourse.do";// 课程修改
	public static final String URL_FRIEND = URL_BASE + "mgetfriends.do";
	public static final String URL_LESSON = URL_BASE + "mindexLesson.do";
	public static final String URL_CATEGORY = URL_BASE + "messayTypeList.do";// 全部课程->分类集合
	public static final String URL_MEMBER_CLUB = URL_BASE + "mrelation.do";// 个人主页->分类显示
	public static final String URL_FOLLOWER = URL_BASE + "mbfollower.do";// 个人主页->关注、取消关注
	public static final String URL_BLACKE = URL_BASE + "mbblacker.do";// 个人主页->拉黑、解除拉黑
	public static final String URL_WEIXIN_PAY = URL_BASE + "mwxpay.do";// 微信支付
	public static final String URL_TARINFO = URL_BASE + "mgetMemberByMemid.do";// TAR聊天头像
	public static final String URL_COACH_LIST = URL_BASE + "mcoachList.do";// 教练列表
	public static final String URL_CLASS_LIST = URL_BASE + "morderlist.do";// 课程订单列表
	public static final String URL_CLASS_MONEY = URL_BASE + "moutBYcoin.do";// 课程支付
	public static final String URL_FOOD_MONEY = URL_BASE + "mfoodoutBYcoin.do";// 配餐支付
	public static final String URL_CLASS_DEL = URL_BASE + "mdeleteByList.do";// 订单删除
	public static final String URL_CLASS_PAY = URL_BASE
			+ "mselectcorderByOrdno.do";// 课程订单继续支付
	public static final String URL_CLASS_PAY_AGAIN = URL_BASE
			+ "mselectcorderByOrdno.do";// 课程订单再来一单
	public static final String URL_FOOD_PAY_AGAIN = URL_BASE
			+ "mselectForderByOrdno.do";// 配餐订单再来一单
	public static final String URL_EVALUATE = URL_BASE + "mmevaluate.do";// 评价
	public static final String URL_HOT_DYNAMIC = URL_BASE + "mhotDynamics.do";// 热门动态
	public static final String URL_HOT_DYNAMIN_RE = URL_BASE
			+ "mselectDynamic.do";// 刷新一条动态的点赞/评论数
	public static final String URL_HOT_ZAN = URL_BASE + "mpointsave.do";// 点赞取消赞

	public static final String URL_MASSAGE_LIST = URL_MASSAGE
			+ "mmassageList.do";// 按摩项目列表
	public static final String URL_MASSAGE_INFO = URL_MASSAGE
			+ "mmassageInfo.do";// 按摩详情
	public static final String URL_MASSAGE_PERSON = URL_MASSAGE
			+ "mmasseurList.do";// 按摩师列表
	public static final String URL_MASSAGE_ORDER = URL_MASSAGE
			+ "msaveMassageOrder.do";// 按摩确认订单
	public static final String URL_PAY = URL_MASSAGE + "maccount.do";// 支付页面数据
	public static final String URL_MASSAGE_MONEY = URL_MASSAGE
			+ "mmassageoutBYcoin.do";// 按摩支付
	public static final String URL_MASSAGE_ORDER_LIST = URL_MASSAGE
			+ "mmassageorderList.do";// 按摩订单列表
	public static final String URL_MASSAGE_ORDER_DEL = URL_MASSAGE
			+ "mdeleteMassageOrder.do";// 删除按摩订单

	public static final String URL_GROUP_LIST = URL_GROUP + "groupList.do";// 所有群列表
	public static final String URL_MY_GROUP_LIST = URL_GROUP + "mygrouplist.do";// 我的群列表
	public static final String URL_GROUP_DEATILS = URL_GROUP + "groupinfo.do";// 群详情
	public static final String URL_GROUP_JOIN = URL_GROUP + "memjoingroup.do";// 入群申请
	public static final String URL_GROUP_QUIT = URL_GROUP + "memquitgroup.do";// 退群申请
	public static final String URL_GROUP_PERSON = URL_GROUP + "mems.do";// 群成员
	public static final String URL_GROUP_SH = URL_GROUP + "nochkmems.do";// 待审核
	public static final String URL_GROUP_YES = URL_GROUP + "accmemjoin.do";// 通过入群申请
	public static final String URL_GROUP_DEL = URL_GROUP + "removemem.do";// 移除会员
	public static final String URL_GROUP_MES = URL_GROUP + "editgroup.do";// 修改群信息
	public static final String URL_GROUP_DYN = URL_GROUP + "groupdynlist.do";// 群动态
	public static final String URL_GROUP_CHAT_LIST = URL_GROUP
			+ "groupchats.do";// 群聊天记录列表

	/*----BEGIN   BEGIN   BEGIN   BEGIN   BEGIN   BEGIN   BEGIN   BEGIN   ------*/

	/**
	 * 订餐列表
	 * http://192.168.1.121:8080/efithealth/mbFoodmers.do?memid=M000439&mertype=all&pageIndex=1&sorttype=sorttype
	 */
	public static final String URL_LUNCH_LIST=URL_BASE+"mbFoodmers.do";

	/**
	 * 订餐详情
	 * 商品id
	 *http://192.168.1.121:8080/efithealth/mgetFoodmers.do?merid=M000019
	 */
	public static final String URL_LUNCH_DETAIL=URL_BASE+"mgetFoodmers.do";


	/*----END   END   END   END   END   END   END   END   END   END   END   ----*/
}
