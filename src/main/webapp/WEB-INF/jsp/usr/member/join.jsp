<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<c:set var="pageTitle" value="회원 가입" />

<%@ include file="/WEB-INF/jsp/common/header.jsp" %>

	<script>
	
		let validLoginId = null;
	
		const joinFormChk = function (form) {
			form.loginId.value = form.loginId.value.trim();
			form.loginPw.value = form.loginPw.value.trim();
			form.loginPwChk.value = form.loginPwChk.value.trim();
			form.name.value = form.name.value.trim();
			
			if (form.loginId.value.length == 0) {
				alert('아이디는 필수 입력 정보입니다');
				form.loginId.focus();
				return false;
			}
			
			if (form.loginId.value != validLoginId) {
				alert('[ ' + form.loginId.value + ' ] 은(는) 사용할 수 없는 아이디입니다.');
				form.loginId.value = '';
				form.loginId.focus();
				return false;
			}
			
			if (form.loginPw.value.length == 0) {
				alert('비밀번호는 필수 입력 정보입니다');
				form.loginPw.focus();
				return false;
			}
			
			if (form.name.value.length == 0) {
				alert('이름은 필수 입력 정보입니다');
				form.name.focus();
				return false;
			}
			
			if (form.loginPw.value != form.loginPwChk.value) {
				alert('비밀번호가 일치하지 않습니다');
				form.loginPw.value = '';
				form.loginPwChk.value = '';
				form.loginPw.focus();
				return false;
			}
			
			return true;
		}
		
		const checkLoginIdDup = function () {
			const loginId = $('input[name="loginId"]').val().trim();
			const loginIdDupChkMsg = $('#loginIdDupChkMsg');
			
			if (loginId.length == 0) {
				loginIdDupChkMsg.removeClass('text-green-500');
				loginIdDupChkMsg.addClass('text-red-500');
				loginIdDupChkMsg.html('아이디를 입력해주세요');
				validLoginId = null;
				return;
			}
			
			// 서버에 중복확인 요청
			$.post('/usr/member/checkLoginIdDup', {
				loginId: loginId
			}, function(data) {
				if (data === '사용 가능한 아이디입니다') {
					loginIdDupChkMsg.removeClass('text-red-500');
					loginIdDupChkMsg.addClass('text-green-500');
					loginIdDupChkMsg.html(data);
					validLoginId = loginId;
				} else {
					loginIdDupChkMsg.removeClass('text-green-500');
					loginIdDupChkMsg.addClass('text-red-500');
					loginIdDupChkMsg.html(data);
					validLoginId = null;
				}
			});
		}
	</script>

	<section class="mt-8">
		<div class="container mx-auto">
			<form action="doJoin" method="post" onsubmit="return joinFormChk(this);">
				<div class="table-box">
					<table class="table">
						<tr>
							<th>아이디</th>
							<td>
								<div class="flex gap-2">
									<input class="input input-neutral flex-1" name="loginId" type="text">
									<button type="button" class="btn btn-neutral btn-outline btn-sm" onclick="checkLoginIdDup();">중복확인</button>
								</div>
								<div id="loginIdDupChkMsg" class="mt-2 text-sm h-5 mx-auto text-center"></div>
							</td>
						</tr>
						<tr>
							<th>비밀번호</th>
							<td><input class="input input-neutral" name="loginPw" type="text" /></td>
						</tr>
						<tr>
							<th>비밀번호 확인</th>
							<td><input class="input input-neutral" name="loginPwChk" type="text" /></td>
						</tr>
						<tr>
							<th>이름</th>
							<td><input class="input input-neutral" name="name" type="text" /></td>
						</tr>
						<tr>
							<td colspan="2"><button class="btn btn-neutral btn-outline btn-sm btn-wide">가입</button></td>
						</tr>
					</table>
				</div>
			</form>
			
			<div class="p-6">
				<div><button class="btn btn-neutral btn-outline btn-xs" onclick="history.back();">뒤로가기</button></div>
			</div>
		</div>
	</section>

<%@ include file="/WEB-INF/jsp/common/footer.jsp" %>