<script setup>
import VerticalBar from "../../components/VerticalBar.vue";
import { ref, reactive, onMounted, nextTick } from "vue";
import * as pdfjsLib from "pdfjs-dist/legacy/build/pdf.mjs";
// import * as pdfjsLib from "pdfjs-dist/build/pdf.mjs";
// import * as pdfjsLib from "pdfjs-dist";

import pdfFile from "../../assets/testpdf.pdf";
import 'pdfjs-dist/web/pdf_viewer.css'

let pdfDoc = reactive({}); // 保存加载的pdf文件流
let pdfPages = ref(0); // pdf文件的页数
//pdf文件的链接
let pdfUrl = ref(pdfFile);
let pdfScale = ref(2.22); // 缩放比例

//获取pdf文档流与pdf文件的页数
const loadFile = async (url) => {
//内部链接引入
  pdfjsLib.GlobalWorkerOptions.workerSrc =
      "../../../node_modules/pdfjs-dist/build/pdf.worker.min.mjs";
  const loadingTask = pdfjsLib.getDocument(url);
  loadingTask.promise.then((pdf) => {
    console.log(pdf);
    pdfDoc = pdf;                 //获取pdf文档流
    pdfPages.value = pdf.numPages;//获取pdf文件的页数
    nextTick(() => {
      renderPage(1);
    });
  });
};

const renderPage = (num) => {
  pdfDoc.getPage(num).then((page) => {
    const canvasId = "pdf-canvas-" + num;
    const canvas = document.getElementById(canvasId);
    const ctx = canvas.getContext("2d");
    const dpr = window.devicePixelRatio || 1;
    const bsr =
        ctx.webkitBackingStorePixelRatio ||
        ctx.mozBackingStorePixelRatio ||
        ctx.msBackingStorePixelRatio ||
        ctx.oBackingStorePixelRatio ||
        ctx.backingStorePixelRatio ||
        1;
    const ratio = dpr / bsr;
    const viewport = page.getViewport({ scale: pdfScale.value });
    canvas.width = viewport.width * ratio;
    canvas.height = viewport.height * ratio;
    canvas.style.width = viewport.width + "px";
    canvas.style.height = viewport.height + "px";
    ctx.setTransform(ratio, 0, 0, ratio, 0, 0);
    const renderContext = {
      canvasContext: ctx,
      viewport: viewport,
    };
    page.render(renderContext);
    if (num < pdfPages.value) {
      renderPage(num + 1);
    }
  });
};

//调用loadFile方法
onMounted( () => {
  loadFile(pdfUrl.value);
});
</script>

<template>
  <div class="outline-container">
    <div class="header">
      <vertical-bar text="课程大纲"></vertical-bar>
    </div>



    <div class="interviewVideo_main" id="videoContainer">
      <!--此处根据pdf的页数动态生成相应数量的canvas画布-->
      <canvas
          v-for="pageIndex in pdfPages"
          :id="`pdf-canvas-` + pageIndex"
          :key="pageIndex"
          style="display: block"
      ></canvas>
    </div>



  </div>
</template>

<style lang="scss" scoped>
.outline-container{
  min-height: calc(100% - 10px);
  .header {
    padding-top: 10px;
    margin-left: 10px;
    //border: 1px solid black;
  }
  .interviewVideo_main{
    padding: 20px;
    margin: 20px;
    border-radius: 5px;
    background-color: #9f7390;
    //color: black;
    //font-size: 14px;
    //min-height: 20px;
  }
}

</style>