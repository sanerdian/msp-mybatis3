let editor_html,editor_js, editor_css;

function loadCurrentPageCode(callback) {
    showHtmlCode();
    if (!currentPage || currentPage.length === 0) {
        layer.msg("请选择有效的页面", {icon: 0, time: 1000});
        return;
    }
    let index = layer.load(1);
    $.get(`../dist/${currentPage}.html?_=${new Date().getTime()}`, function (res) {
        let html = $(res).find("#app").html();
        const regJs = /<script id="js_content">([\S\s]*)<\/script>/;
        regJs.test(res);
        let js = RegExp.$1;
        const regCss = /<style id="css_content">([\S\s]*)<\/style>/;
        regCss.test(res);
        let css = RegExp.$1;
        $("#editor_code form.code_form").show();
        loadCode(editor_html, html, 0);
        loadCode(editor_css, css, 1);
        loadCode(editor_js, js, 2);
        if ($("#html_code_item").hasClass("layui-this")) {
            showHtmlCode();
        } else if ($("#js_code_item").hasClass("layui-this")) {
            showJsCode();
        } else if ($("#css_code_item").hasClass("layui-this")) {
            showCssCode();
        } else {
            showHtmlCode();
        }
        if (typeof callback === "function") {
            callback();
        }
        editor_html.clearHistory();
        editor_js.clearHistory();
        editor_css.clearHistory();
        layer.close(index);
    });
}

function initCodeEditor() {
    $("#product_item").click(function () {
        $("#editor_content").show();
        $("#tools").show();
        $("#editor_code").hide();
        emptyEditor();
        loadCurrentPage(true);
    });
    $("#code_item").click(function () {
        $("#editor_content").hide();
        $("#tools").hide();
        loadCurrentPageCode();
    });
    $("#html_code_item").click(function () {
        showHtmlCode();
    });
    $("#css_code_item").click(function () {
        showCssCode();
    });
    $("#js_code_item").click(function () {
        showJsCode();
    });
    editor_html = initEditor("html_code", "htmlmixed");
    editor_js = initEditor("js_code", "javascript");
    editor_css = initEditor("css_code", "css");
}

function showHtmlCode() {
    $("#editor_code").show();
    $("#editor_code form.code_form").hide();
    $("#html_code").parent().show();
}

function showJsCode() {
    $("#editor_code").show();
    $("#editor_code form.code_form").hide();
    $("#js_code").parent().show();
}

function showCssCode() {
    $("#editor_code").show();
    $("#editor_code form.code_form").hide();
    $("#css_code").parent().show();
}

function initEditor(elementId ,mode) {
    let editor_code = $("#editor_code");
    editor_code.show();
    CodeMirror.commands.autocomplete = function (cm) {
        CodeMirror.simpleHint(cm, CodeMirror.javascriptHint);
    }
    let editor = CodeMirror.fromTextArea(document.getElementById(elementId), {
        lineNumbers: true,
        indentUnit: 4,
        mode: mode,
        matchBrackets: true,
        autoRefresh: true,
        extraKeys: {"Alt-D": "autocomplete"},
        onBlur: function () {
            editor.save()
        }
    });
    CodeMirror.commands["selectAll"](editor);
    editor_code.hide();
    return editor;
}

function loadCode(editor, code, codeIndex) {
    if (!codeIndex || codeIndex > 2 || codeIndex < 0) {
        codeIndex = 0;
    }
    let editor_code = $("#editor_code");
    editor_code.show();
    editor.setValue(code);
    editor.setOption("theme", "monokai");
    let num = editor.lineCount();
    editor.autoFormatRange({line: 0, ch: 0}, {line: num});
    editor.setCursor({line: 0, ch: 0});
    let codemirrorScroll = document.getElementsByClassName('CodeMirror-scroll');
    let height = 1536;
    if(height < codemirrorScroll[codeIndex].scrollWidth + 20){
        height = codemirrorScroll[codeIndex].scrollWidth + 20;
    }
    editor.setSize(height, codemirrorScroll[codeIndex].scrollHeight + 20);
}

function getCode(editor) {
    return editor.getValue();
}
