layui.use(['element', 'jquery', 'layer', 'form'], function () {
    let layer = layui.layer;
    let $ = layui.jquery;
    let clientId = createUUID();
    let verificationFlag = false;
    $().ready(function () {
        let $submitBtn = $('#submitBtn');
        new Promise(function (resolve) {
            $.post('/index/verificationRegister', {"clientId": clientId}, function (rtn) {
                resolve(rtn.code === "200");
            });
        }).then(function (r) {
            if (r === true) {
                let SlideVerifyPlug = window.slideVerifyPlug;
                let slideVerify = new SlideVerifyPlug('#verifyWrap', {
                    wrapWidth: '270',
                    getSuccessState: function () {
                        verificationFlag = slideVerify.slideFinishState;
                        $.post('/index/verificationRegister', {"clientId": clientId}, function (rtn) {
                            if (rtn.code === "200") {
                                $submitBtn.attr("disabled", false);
                                $submitBtn.css({'background-color': '#EF4300'});
                                $submitBtn.css({'border-color': '#FF730E'});
                            }
                        });
                    }
                });
            } else {
                layer.msg("The network is busy, please refresh and try again");
            }
        });
        $submitBtn.on("click", function () {
            if (!verificationFlag) {
                layer.msg("Please complete verification");
                return false;
            }
            let userName = $('#userName').val();
            let password = $('#password').val();
            if (userName === '') {
                layer.msg("Please input a user name");
                return false;
            }
            if (password === '') {
                layer.msg("Please input a password");
                return false;
            }
            let index = layer.load(2, {time: 10 * 1000});
            $submitBtn.attr("disabled", true);
            let data = {
                "clientId": clientId,
                "userName": userName,
                "password": password
            };
            $.post('/index/login', data, function (rtn) {
                layer.msg(rtn.msg);
                if (rtn.code === "200") {
                    sessionStorage.setItem("user", JSON.stringify(rtn.data));
                    window.location = "/page/index.html";
                }
            });
            layer.close(index);
            $submitBtn.html("Sign in");
            $submitBtn.attr("disabled", false);
            return false;
        });
    })
    ;
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