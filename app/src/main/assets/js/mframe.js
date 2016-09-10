//请求URL根目录
//var serviceUrl="http://192.168.1.185:18080/efithealth/";
var serviceUrl="http://192.168.1.121:8080/efithealth/";
//var serviceUrl = "http://192.168.1.88:8188/efithealth/";
//var serviceUrl = "http://192.168.1.137:8080/efithealth/";
var parmData = getUrlParam(window.location.href);
var pageIndex = 1; //默认页码
var curOS = getcurrentOS();
var noResultEle = "<div class='notips' data-role='content' style='background:#fff;'><div class='notips_down_1'><img src='images/page/error.png' width='50%' style='margin:0 auto;margin-bottom:73px;'><p class='black' id='noData_title'>No Result Found</p><a href='' data-role='none' rel='external'><p id='noData_content'>Your internet seems too slow to</p></a></div></div>";
var foodPlatPhoto = "images/page/foodmerPlat.png"//配餐平台头像
var lock = true; //同步锁(暂未使用)
//var ajaxParam; //ajax参数字符串
//zhouhong 2016-02-29 delete
/*
  $(document).bind('mobileinit', function () {
            $.mobile.pushStateEnabled = false;
        });*/
/**
 * 发送ajax请求
 * @param {Object} url 请求地址
 * @param {Object} data 参数JSON
 * @param {Object} success 回调函数
 */
//==性能测试结果本方法:JS执行时间:312.14ms,页面总加载时间:531.8ms;(未引用jQuery.js)
//==性能测试结果jquery方法:JS执行时间:405.14ms,页面总加载时间:589.52ms;
_ajax = function(url, data, success) {
	var xmlHttp, ajaxParam,
		/**
		 * 回调方法
		 */
		loadCallBack = function() {
			try {
				if (xmlHttp.readyState == 4) {
					if (xmlHttp.status == 200) {
						if (xmlHttp.responseText != null && xmlHttp.responseText != "") {
							success(JSON.parse(xmlHttp.responseText));
						}
					}
				}
				if (xmlHttp.readyState == 1) {
					//alert("正在加载连接对象......");
				}
				if (xmlHttp.readyState == 2) {
					//alert("连接对象加载完毕。");
				}
				if (xmlHttp.readyState == 3) {
					//alert("数据获取中......");
				}
			} catch (e) {
				//alert(e);
				console.log(e);
			}
		},
		/**
		 * 创建XMLHttpRequest对象
		 */
		createXMLHttpRequest = function() {
			try {
				xmlHttp = new XMLHttpRequest();
			} catch (failed) {
				xmlHttp = false;
			}
			if (!xmlHttp) {
				alertMessage("error", "建立连接失败");
			}
		},
		/**
		 * 将json转换为URL格式参数
		 * @param {Object} _json
		 */
		jsonToParam = function(_json) {
			for (prefix in _json) {
				add(prefix, _json[prefix]);
			}
		},
		/**
		 * 拼接参数字符串
		 * @param {Object} key
		 * @param {Object} value
		 */
		add = function(key, value) {
			value = isFunction(value) ? value() : (value == null ? "" : value);
			!ajaxParam ? ajaxParam = "?" + encodeURIComponent(key) + "=" + encodeURIComponent(value) : ajaxParam += "&" + encodeURIComponent(key) + "=" + encodeURIComponent(value);
		};

	//创建XMLHttpRequest对象
	createXMLHttpRequest();
	//将json转换为url参数格式
	jsonToParam(data);
	if (!ajaxParam) {
		//设置一个事件处理器，当XMLHttp状态发生变化，就会出发该事件处理器，由他调用
		//callback指定的javascript函数
		xmlHttp.onreadystatechange = loadCallBack;
		//设置其调用的参数（提交的方式，请求的的url，请求的类型（异步请求））
		xmlHttp.open("GET", url, true); //true表示发出一个异步的请求。
		xmlHttp.send(null);
	} else {
		xmlHttp.onreadystatechange = loadCallBack;
		url += ajaxParam //拼接参数
		xmlHttp.open("POST", url, true);
		xmlHttp.setRequestHeader("Content-Type", "application/x-www-form-urlencoded;");
		xmlHttp.send(null);
	}
}

var myScroll = {
	//文档高度
	documentTop: function() {
		var scrollTop = 0,
			bodyScrollTop = 0,
			documentScrollTop = 0;
		if (document.body) {
			bodyScrollTop = document.body.scrollTop;
		}
		if (document.documentElement) {
			documentScrollTop = document.documentElement.scrollTop;
		}
		scrollTop = (bodyScrollTop - documentScrollTop > 0) ? bodyScrollTop : documentScrollTop;
		return scrollTop;
	},
	//可视窗口高度
	windowHeight: function() {
		var windowHeight = 0;
		if (document.compatMode == "CSS1Compat") {
			windowHeight = document.documentElement.clientHeight;
		} else {
			windowHeight = document.body.clientHeight;
		}
		return windowHeight;
	},
	//滚动条滚动高度
	scrollHeight: function() {
		var scrollHeight = 0,
			bodyScrollHeight = 0,
			documentScrollHeight = 0;
		if (document.body) {
			bodyScrollHeight = document.body.scrollHeight;
		}
		if (document.documentElement) {
			documentScrollHeight = document.documentElement.scrollHeight;
		}
		scrollHeight = (bodyScrollHeight - documentScrollHeight > 0) ? bodyScrollHeight : documentScrollHeight;
		return scrollHeight;
	}

}
/**
 * 获取该元素的index(在父元素下第几个可见元素)
 * @param {Object} ele
 */
function getIndex(ele){
	var tabIndex = 0;
	while (ele=ele.previousElementSibling){
		if(ele&&ele.style.display!=="none"){
			tabIndex++;
		}
	}
	return tabIndex;
}

/**
 * 浏览图片
 */
function imgView(urlStr, imgList, index) {
	console.log(urlStr+","+imgList);
	if (window.mobile) {
		switch (getcurrentOS()) {
			case 'a':
				window.mobile.imgView(urlStr, index);
				break;
			case 'i':
				window.mobile.imgView(imgList, index);
				break;
			default:
				break;
		}
	}
}


/**
 * 返回
 */
function backExe(){
	!window.mobile||window.mobile.backExe();
}

/**
 * 判断是否函数 
 * 
 * @param {Object} obj
 * @return boolean
 */
function isFunction(obj) {
	return getType(obj) === "function";
}
/**
 * 获得对象类型 
 * @param {Object} obj
 * @return function/object
 */
function getType(obj) {
	if (obj == null) {
		return String(obj);
	}
	return typeof obj === "object" || typeof obj === "function" ?
		class2type[core_toString.call(obj)] || "object" :
		typeof obj;
}

/**
 * 获得当前操作系统
 */
function getcurrentOS() {
	var u = navigator.userAgent;
	if (u.indexOf('Android') > -1 || u.indexOf('Linux') > -1) { //安卓手机
		return "a";
	} else if (u.indexOf('iPhone') > -1) { //苹果手机
		return "i";
	} else if (u.indexOf('Windows Phone') > -1) { //winphone手机
		return "w";
	}
}

function slideTabChange(index, curid) {

}

function createNewWindow(page){
	if (window.mobile) {
		window.mobile.popNewWindow(page);
	} else {
		window.location.href = page;
	}
}

/**
 * 拨打电话方法
 */
function callUp(el,phoneNumber){
	if(window.mobile&&getcurrentOS()==="a"){
		window.mobile.callUp(""+phoneNumber);
	}else{
		location.href="tel:"+phoneNumber;
	}
}

function loadPageData(){
	var titleEl = document.querySelector("title");
	window.mobile.loadPageData(document.title,titleEl.getAttribute("isback"),titleEl.getAttribute("btn"),titleEl.getAttribute("navbar"));
}

/**
 * 根据URL解析上个页面传来的参数
 * @returns {{}} 参数json
 */
function getUrlParam(string) {
	var obj = {};
	if (string.indexOf("?") != -1) {
		var string = string.substr(string.indexOf("?") + 1);
		var strs = string.split("&");
		for (var i = 0; i < strs.length; i++) {
			var tempArr = strs[i].split("=");
			obj[tempArr[0]] = decodeURI(tempArr[1]);
		}
	}
	return obj;
}

function getNoDataDiv(title, content) {
	var eleStr = "<div class='notips' data-role='content' style='background:#fff;'><div class='notips_down_1'>";
	eleStr += "<img src='images/page/error.png' width='50%' style='margin:0 auto;margin-bottom:73px;'><p class='black'>";
	eleStr += title;
	eleStr += "</p><a href='' data-role='none' rel='external'><p id='noData_content'>";
	eleStr += content + "</p></a></div></div>";
	return eleStr;
}
/**
 * 
 * @param {Object} type 提示类型:success成功，error错误，warn警告, message(无文本)
 * @param {Object} content
 */
function alertMessage(type, content) {
	try {
		window.mobile.alertInfo(type, content);
	} catch (e) {
		alert(content);
	}
}

function synchrolock(fn) {
	if (lock) {
		lock = false;
		fn.call();
	} else {
		alertMessage("message", "");
	}
}

function alertInfo(content) {
	alertMessage("warn", content)
}

//zhouhong 2016-02-29 delete
/*function getMemidFromiOS(memid){
	alert(memid);
}
*/

function getPageInfo() {
	return document.title + "|" + $("title").attr("isback") + "|" + $("title").attr("btn") + "|" + $("title").attr("navbar");
}

function getMemid() {
	var memid = "";
	try {
		if (parmData.memid) {
			memid = parmData.memid;
		} else {
			memid = window.mobile.getmemid();
		}
		//alert("来自手机端，Memid="+memid);
	} catch (e) {
		memid = "M000014"; //测试用,与移动段整合时注掉这句
	}
	return memid;
}

//显示错误页面
function gotoErrorPage(errorContent) {
	alert("出错提示页面开发中:" + errorContent);
	var page = "errorPage.html?errorContent=" + errorContent;
	window.location.href = page;
}

//显示无数据页面
function gotoNodataPage(title, content1, content2) {
	//contentkey:订单列表、订单详情
	var page = "noSearchResult.html?content_1=" + content1 + "&title=" + title + "&content2=" + content2;
	window.location.href = page;
}
/**
 * 刷新上一页
 */
function freshPrev(){
	!window.mobile||window.mobile.isrefresh();
}

/**
 * 返回上一页
 */
function backPageExe() {
	history.go(-1);
}

/**
 * 返回主页
 */
function homePageExe() {
	this.location = "mindex.do"
}

//重置H5底栏
function initNavBar() {
	$("div[data-role='footer']").attr("style", "bottom:0;");
}

//格式化时间 yyyy-MM-dd
function formatdate(value) {
	return (new Date(value.time)).format("yyyy-MM-dd");
}

//加载滑道
function initSlideTab() {
	var page = 'pagenavi';
	var mslide = 'slider';
	var mtitle = 'emtitle';
	arrdiv = 'arrdiv';
	var as = document.getElementById(page).getElementsByTagName('a');
	var tt = new TouchSlider({
		id: mslide,
		'auto': '-1',
		fx: 'ease-out',
		direction: 'left',
		speed: 300,
		timeout: 0,
		'before': function(index) {
			var as = document.getElementById(this.page).getElementsByTagName('a');
			as[this.p].className = '';
			as[index].className = 'active';
			this.p = index;
			var txt = as[index].innerText;
			$("#" + this.page).parent().find('.emtitle').text(txt);
			var txturl = as[index].getAttribute('href');
			var turl = txturl.split('#');
			$("#" + this.page).parent().find('.go_btn').attr('href', turl[1]);

		}
	});
	tt.page = page;
	tt.p = 0;
	//console.dir(tt); console.dir(tt.__proto__);
	for (var i = 0; i < as.length; i++) {
		(function() {
			var j = i;
			as[j].tt = tt;
			as[j].onclick = function() {
				this.tt.slide(j);
				return false;
			}
		})();
	}
}

//时间格式化
Date.prototype.format = function(format) {
	if (!format) {
		format = "yyyy-MM-dd hh:mm:ss";
	}
	var o = {
		"M+": this.getMonth() + 1, // month
		"d+": this.getDate(), // day
		"h+": this.getHours(), // hour
		"m+": this.getMinutes(), // minute
		"s+": this.getSeconds(), // second
		"q+": Math.floor((this.getMonth() + 3) / 3), // quarter
		"S": this.getMilliseconds()
			// millisecond
	};
	if (/(y+)/.test(format)) {
		format = format.replace(RegExp.$1, (this.getFullYear() + "")
			.substr(4 - RegExp.$1.length));
	}
	for (var k in o) {
		if (new RegExp("(" + k + ")").test(format)) {
			format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k] : ("00" + o[k]).substr(("" + o[k]).length));
		}
	}
	return format;
};