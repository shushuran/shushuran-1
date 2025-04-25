// 从URL查询参数中获取paperId
function getQueryParam(param) {
	var urlParams = new URLSearchParams(window.location.search);
	return urlParams.get(param);
}

// 页面加载时设置paperId输入框的值
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
	var paperId = getQueryParam('id'); // 从URL中获取论文ID
	if (!paperId) {
		alert('论文ID无效！');
		window.location.replace('index.html');
		return;
	}

	// 设置ID输入框的值
	$('#paperId').val(paperId);

	// 获取论文详情并填充表单
	$.ajax({
		url: `http://localhost:8080/paper/findbyid?id=${paperId}`,
		type: 'GET',
		headers: {
			'Authorization': authToken
		},
		success: function (response) {
			if (response.code === 0 && response.data) {
				const paper = response.data;
				// 填充表单数据
				$('#title').val(paper.title);
				$('#author').val(paper.author);
				$('#text').val(paper.text);
				$('#Pushdate').val(formatDateTimeLocal(paper.Pushdate));
				$('#Updatedate').val(formatDateTimeLocal(paper.Updatedate));
				$('#type').val(paper.type);
			} else {
				alert('获取论文详情失败：' + response.message);
				window.location.replace('page.html');
			}
		},
		error: function (xhr, status, error) {
			if (xhr.status === 401) {
				alert('用户凭证已失效，请重新登录！');
				localStorage.removeItem('authToken');
				window.location.href = 'login.html';
			} else {
				alert('请求失败：' + xhr.responseText);
				window.location.replace('index.html');
			}
		}
	});

	// 表单提交事件
	$('#editPaperForm').on('submit', function (event) {
		event.preventDefault(); // 阻止表单的默认提交行为

		// 收集表单数据
		var formData = {
			id: $('#paperId').val(),
			title: $('#title').val(),
			text: $('#text').val(),
			type: $('#type').val()
		};

		// 添加确认框
		if (!confirm('您确定要更新这篇论文吗？')) {
			return; // 如果用户点击取消，则不执行后续操作
		}

		// 发送PUT请求到后端更新论文信息
		$.ajax({
			url: 'http://localhost:8080/paper/update',
			type: 'PUT',
			contentType: 'application/json',
			headers: {
				'Authorization': authToken
			},
			data: JSON.stringify(formData),
			success: function (response) {
				if (response.code === 0) {
					alert('更新成功！');
					window.location.href = 'page.html';
				} else {
					alert('更新失败：' + response.message);
				}
			},
			error: function (xhr, status, error) {
				alert('请求失败：' + xhr.responseText);
			}
		});
	});
});

// 将日期格式化为 datetime-local 输入框所需的格式
function formatDateTimeLocal(dateString) {
	const date = new Date(dateString);
	const year = date.getFullYear();
	const month = String(date.getMonth() + 1).padStart(2, '0');
	const day = String(date.getDate()).padStart(2, '0');
	const hours = String(date.getHours()).padStart(2, '0');
	const minutes = String(date.getMinutes()).padStart(2, '0');
	return `${year}-${month}-${day}T${hours}:${minutes}`;
}