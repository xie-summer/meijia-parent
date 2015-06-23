$(function(){
    var secId = localStorage['sec_id'];
    var mobile = localStorage['sec_mobile'];
    //获取用户消息列表
    getSecInfo(secId,mobile);

}());

//获取用户消息列表
function getSecInfo(secId,mobile){
    $.ajax({
        type:"POST",
        url:siteAPIPath+"sec/get_secinfo.json",
        dataType:"json",
        cache:false,
        data:"sec_id="+secId+"&mobile="+mobile,
        success : onListSuccess,
        error : onListError
    });
}
function onListSuccess(data, status){
  if(data.status != "0"){
    if (data.status =="999")
        alert(data.msg);
    else
    	/*alert(data);
        alert("目前无法获取用户消息列表，请稍后再试。");*/
    return;
  }
  var sec = data.data;
  if(sec==''){
	$("#moreInfo").css("display","none");
  }
  $("#name").val(sec.name);
  $("#nickName").val(sec.nickName);
 // $("#sex").text(sec.sex);
  $("#mobile").val(sec.mobile);
  $("#birthDay").val(sec.birthDay);
  $("#cityName").val(sec.cityName);
}
function onListError(data, status){
}
$("#mine_info_edit").bind("click",function(){
	alert("表单提交");
/*	window.location.href="wx-mine-info_edit.html";
*/})
