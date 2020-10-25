// Based on https://stackoverflow.com/questions/63179813/how-to-run-the-monaco-editor-from-a-cdn-like-cdnjs
require.config({
    paths: {
        "vs": "https://cdnjs.cloudflare.com/ajax/libs/monaco-editor/0.21.2/min/vs"
    }
});
window.MonacoEnvironment = {
    getWorkerUrl: () => proxy
};

let proxy = URL.createObjectURL(new Blob([`
    self.MonacoEnvironment = {
        baseUrl: "https://cdnjs.cloudflare.com/ajax/libs/monaco-editor/0.21.2/min"
    };
    importScripts("https://cdnjs.cloudflare.com/ajax/libs/monaco-editor/0.21.2/min/vs/base/worker/workerMain.min.js");
`], {type: "text/javascript"}));

require(["vs/editor/editor.main"], function () {
    let container = $("#editor");

    if (container) {
        let code = container.find("pre").text();
        let editor = monaco.editor.create(container, {
            value: code,
            language: "lua",
            theme: "vs-dark"
        });

        window.onresize = function () {
            editor.layout();
        };
    }
});
