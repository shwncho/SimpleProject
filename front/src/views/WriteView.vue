<script setup lang="ts">
  import {ref} from "vue";
  import axios from 'axios';
  import {useRouter} from "vue-router";


  const title =ref("")
  const content=ref("")
  const router = useRouter();

  const write=function (){
    axios.post("http://localhost:8080/posts",{
      title: title.value,
      content: content.value
    })
    .then(()=>{
      /**
       * push 대신 replace 사용
       * => 글 작성하고 홈 화면에서 뒤로가기 눌렀을 때 다시 글 작성화면으로 라우팅 되는거 방지할 수 있다.
       */
      router.replace({name: "home"});
    });
  }
</script>

<template>
  <div>
    <input v-model="title" placeholder="제목을 입력해주세요" />
  </div>

  <div class="mt-2">
    <el-input v-model="content" type="textarea" rows="15"/>
  </div>

  <div class="mt-2">
    <el-button type="primary" @click="write()">등록</el-button>
  </div>

</template>

<style>

</style>