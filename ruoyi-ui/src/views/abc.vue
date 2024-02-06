<template>
  <div class="login">
    <el-form class="login-form">
      <el-form-item label="账号">
        <el-input type="text" v-model="loginForm.username"></el-input>
      </el-form-item>
      <el-form-item label="密码">
        <el-input type="password" v-model="loginForm.password"></el-input>
      </el-form-item>
      <el-button type="primary" round @click="denglu">登录</el-button>
    </el-form>
  </div>
</template>

<script>
export default {
  name: 'abc',
  data() {
    return {
      loginForm: {
        username: 'shine',
        password: 'shine123'
      }
    }
  },
  methods: {
    denglu() {
      console.log(111)
      this.$store.dispatch("Login", this.loginForm).then(() => {
        console.log('正常')
        this.$router.push({ path: this.redirect || "/" }).catch(()=>{});
      }).catch(() => {
        console.log('异常')
      });
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>

.login {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100%;
  background-image: url("../assets/images/login-background.jpg");
  background-size: cover;
}

.login-form {
  border-radius: 6px;
  background: #ffffff;
  width: 400px;
  padding: 25px 25px 5px 25px;

  .el-input {
    height: 38px;

    input {
      height: 38px;
    }
  }

  .input-icon {
    height: 39px;
    width: 14px;
    margin-left: 2px;
  }
}
</style>
