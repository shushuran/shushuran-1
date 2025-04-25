$(document).ready(function() {
    const authToken = localStorage.getItem('authToken'); // 从本地存储获取token
    const usernameLink = $('#username-link'); // 用户名链接
    const usernameText = $('#username-text'); // 用户名文本

    // 检查是否登录
    if (!authToken) {
        // 未登录时，显示“未登录”文本，并设置点击跳转到登录页面
        usernameText.text('未登录');
        usernameLink.attr('href', 'login.html');
        usernameLink.click(function(event) {
            event.preventDefault(); // 阻止默认跳转行为
            window.location.href = 'login.html'; // 手动跳转到登录页面
        });
    } else {
        // 已登录时，获取用户名并显示
        $.ajax({
            url: 'http://localhost:8080/user/findusername', // 获取用户名的API
            type: 'GET',
            headers: {
                'Authorization': authToken
            },
            success: function(response) {
                if (response && response.data) {
                    // 显示用户名
                    usernameText.text(response.data);

                    // 点击用户名跳转到用户详情页面
                    usernameLink.attr('href', 'userinfo.html');
                    usernameLink.click(function(event) {
                        window.location.href = 'userinfo.html'; // 手动跳转到用户详情页面
                    });
                } else {
                    alert('获取用户名失败！');
                }
            },
            error: function(xhr, status, error) {
                console.error('请求失败: ' + error);
                if (xhr.status === 401) {
                    alert('用户凭证已失效，请重新登录！');
                    localStorage.removeItem('authToken'); // 清除无效的token
                    window.location.href = 'login.html'; // 跳转到登录页面
                }
            }
        });
    }
    loadLatestPapers(authToken);
});
// 获取最新论文数据并渲染到页面
function loadLatestPapers() {
    $.ajax({
        url: 'http://localhost:8080/paper/latest-papers', // Spring Boot 后端接口地址
        type: 'GET',

        success: function(response) {
            if (response) {
                // 更新天气预警模块
                if (response['天气预警']) {
                    $('#weather-warning .content').text(response['天气预警'].text);
                }
                // 更新病虫害防治模块
                if (response['病虫害防治']) {
                    $('#pest-control .content').text(response['病虫害防治'].text);
                }
                // 更新市场行情模块
                if (response['市场行情']) {
                    $('#market-trend .content').text(response['市场行情'].text);
                }
                // 更新种植技术模块
                if (response['种植技术']) {
                    $('#planting-tech .content').text(response['种植技术'].text);
                }
            } else {
                console.error('未获取到数据');
            }
        },
        error: function(xhr, status, error) {
            console.error('请求失败:', error);
        }
    });
}

