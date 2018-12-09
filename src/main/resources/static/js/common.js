let layer = layui.layer;
let laytpl = layui.laytpl;
let form = layui.form;
let element = layui.element;
let laypage = layui.laypage;
let table = layui.table;
let flow = layui.flow;
let laydate = layui.laydate;

function renderAll() {
    element.render();
    form.render();
}

function isEmpty(obj) {
    if (typeof (obj) === "undefined") {
        return true;
    }
    if (obj === "undefined") {
        return true;
    }
    if (obj === "") {
        return true;
    }
    if (!obj && obj !== 0 && obj !== '' && obj == null) {
        return true;
    }
    if (Array.prototype.isPrototypeOf(obj) && obj.length === 0) {
        return true;
    }
    return Object.prototype.isPrototypeOf(obj) && Object.keys(obj).length === 0;
}

function getJsonFromSessonStorage(key) {
    return JSON.parse(sessionStorage.getItem(key));
}

(function ($) {
    $.fn.extend({
        initForm: function (options) {
            let defaults = {
                jsonValue: "",
                isDebug: false
            };
            let setting = $.extend({}, defaults, options);
            let form = this;
            let jsonValue = setting.jsonValue;
            if ($.type(jsonValue) === "string") {
                jsonValue = $.parseJSON(jsonValue);
            }
            if (!$.isEmptyObject(jsonValue)) {
                let debugInfo = "";
                $.each(jsonValue, function (key, value) {
                    if (setting.isDebug) {
                        console.log("name:" + key + "; value:" + value);
                        debugInfo += "name:" + key + "; value:" + value + " || ";
                    }
                    let formField = form.find("[name='" + key + "']");
                    if ($.type(formField[0]) === "undefined") {
                        if (setting.isDebug) {
                            console.log("can not find name:[" + key + "] in form!");
                        }
                    } else {
                        let fieldTagName = formField[0].tagName.toLowerCase();
                        if (fieldTagName === "input") {
                            if (formField.attr("type") === "radio") {
                                $("input:radio[name='" + key + "'][value='" + value + "']").attr("checked", "checked");
                            } else if (formField.attr("type") === "checkbox") {
                                if (value) {
                                    $("input:checkbox[name='" + key + "']").attr("checked", "checked");
                                } else {
                                    $("input:checkbox[name='" + key + "']").removeAttr("checked")
                                }
                            } else {
                                formField.val(value);
                            }
                        } else if (fieldTagName === "select") {
                            formField.val(value);
                        } else if (fieldTagName === "textarea") {
                            formField.val(value);
                        } else {
                            formField.val(value);
                        }
                    }
                });
                if (setting.isDebug) {
                    console.log("debugInfo is :" + debugInfo);
                }
            }
            return form;
        }
    });

    $.fn.extend({
        resetForm: function () {
            let $form = this;
            $form[0].reset();
            $('input:radio').attr('checked', false);
            renderAll();
            return $form;
        }
    });
})(jQuery);

function logout() {
    layer.confirm('Do you confirm logout?', {
        btn: ['Yes', 'No']
    }, function () {
        clearLoginInfo();
        window.location.href = "/";
    }, function (index) {
    });
}

function clearLoginInfo() {
    sessionStorage.removeItem("token");
    sessionStorage.removeItem("exp");
    sessionStorage.clear();
}
