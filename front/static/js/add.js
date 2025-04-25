$(document).ready(function () {
	const authToken = localStorage.getItem('authToken');
	if (!authToken) {
		alert('请先登录！');
		window.location.href = 'login.html';
		return;
	}
	// 检查用户状态
	checkUserStatus();

	function checkUserStatus() {
		$.ajax({
			url: 'http://localhost:8080/user/status',
			type: 'GET',
			headers: {
				'Authorization': authToken // 传递token
			},
			success: function (response) {
				if (response.code === 0 && response.data === 1) {
					// 用户状态为1，允许访问页面
					loadPapers(currentPage, pageSize);
				} else {
					// 用户状态不为1，重定向到无权访问页面或登录页面
					alert('您无权访问此页面！');
					window.location.href = 'index.html'; // 或者跳转到登录页面
				}
			},
			error: function (xhr, status, error) {
				alert('获取用户状态失败，请重试！');
				window.location.href = 'login.html';
			}
		});
	}
	// 表单提交事件
	$('#paperForm').on('submit', function (event) {
		event.preventDefault(); // 阻止表单的默认提交行为

		// 收集表单数据
		var paperData = {
			title: $('#title').val(),
			text: $('#text').val(),
			type: $('#type').val()
		};

		// 添加确认框
		if (!confirm('您确定要添加这篇论文吗？')) {
			return; // 如果用户点击取消，则不执行后续操作
		}

		// 发送POST请求到后端添加论文
		$.ajax({
			url: 'http://localhost:8080/paper/add',
			type: 'POST',
			contentType: 'application/json',
			headers: {
				'Authorization': authToken
			},
			data: JSON.stringify(paperData),
			success: function (response) {
				if (response.code === 0) {
					$('#result').html('<div class="alert alert-success">论文添加成功！</div>');
					$('#paperForm')[0].reset(); // 清空表单
				} else {
					$('#result').html('<div class="alert alert-danger">' + response.message + '</div>');
				}
			},
			error: function (xhr, status, error) {
				if (xhr.status === 401) {
					alert('用户凭证已失效，请重新登录！');
					localStorage.removeItem('authToken');
					window.location.href = 'login.html';
				} else {
					var errorMessage = xhr.responseJSON ? xhr.responseJSON.message : '请求失败';
					$('#result').html('<div class="alert alert-danger">' + errorMessage + '</div>');
				}
			}
		});
	});
});