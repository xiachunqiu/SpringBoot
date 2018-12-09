layui.use(['element', 'jquery', 'layer', 'form'], function () {
    let layer = layui.layer;
    let $ = layui.jquery;
    let clientId = createUUID();
    $().ready(function () {
        initLoginCode();

        function initLoginCode() {
            $.ajax({
                url: "/index/gtRegister",
                type: "get",
                dataType: "json",
                data: {
                    "client-Id": clientId
                },
                success: function (data) {
                    initGeetest({
                        gt: data.gt,
                        challenge: data.challenge,
                        offline: !data.success,
                        new_captcha: true,
                        https: true
                    }, handler);
                }
            });
        }

        function handler(captchaObj) {
            let $submitBtn = $('#submitBtn');
            $submitBtn.on("click", function () {
                let userName = $('#userName').val();
                let password = $('#password').val();
                if (userName === '') {
                    layer.msg("请输入用户名");
                    return false;
                }
                if (password === '') {
                    layer.msg("请输入密码");
                    return false;
                }
                let result = captchaObj.getValidate();
                if (!result) {
                    layer.msg('请先完成验证');
                    return false;
                }
                let index = layer.load(2, {time: 10 * 1000});
                $submitBtn.attr("disabled", true);
                let data = {
                    "geetest_challenge": result.geetest_challenge,
                    "geetest_validate": result.geetest_validate,
                    "geetest_seccode": result.geetest_seccode,
                    "client-Id": clientId,
                    "user-Id": userName,
                    "userName": userName,
                    "password": password
                };
                $.post('/index/login', data, function (rtn) {
                    layer.msg(rtn.msg);
                    if (rtn.success) {
                        window.location = "/page/index.html";
                    }
                });
                layer.close(index);
                $submitBtn.html("Sign in");
                $submitBtn.attr("disabled", false);
                return false;
            });
            captchaObj.appendTo("#captcha");
            captchaObj.onReady(function () {
                $("#wait").hide();
            });
        }
    });
});

function createUUID() {
    let CHARS = '0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz'.split('');
    let chars = CHARS, uuid = [], i;
    let r;
    uuid[8] = uuid[13] = uuid[18] = uuid[23] = '-';
    uuid[14] = '4';
    for (i = 0; i < 36; i++) {
        if (!uuid[i]) {
            r = 0 | Math.random() * 16;
            uuid[i] = chars[(i === 19) ? (r & 0x3) | 0x8 : r];
        }
    }
    return uuid.join('');
}