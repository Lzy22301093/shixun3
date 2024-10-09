import request from '../utils/request'
//
// // 注册接口
// export const userRegisterService = ({ username, password, repassword }) =>
//     request.post('/api/reg', { username, password, repassword })

// 登录接口
export const userLoginService = ({ username, password }) =>
    request.post('/api/login', { username, password })

// 获取用户基本信息
export const userGetInfoService = () => request.get('/my/userinfo')

// 更新用户基本信息
export const userUpdateInfoService = ({ id, nickname, email }) =>
    request.put('/my/userinfo', { id, nickname, email })

// 更新用户头像
export const userUpdateAvatarService = (avatar) =>
    request.patch('/my/update/avatar', { avatar })

// 更新用户密码
export const userUpdatePasswordService = ({ old_pwd, new_pwd, re_pwd }) =>
    request.patch('/my/updatepwd', { old_pwd, new_pwd, re_pwd })
//视频播放请求
export const userGetVideoService = () =>
    request.get('/api/video', { responseType: 'blob' })

// // 获取作业列表接口
// export const getHomeworkListService = () => {
//     return request.get('/api/homework/list'); // 请求作业列表的API地址
// };
//
// // 提交作业接口
// export const submitHomeworkService = (homework) => {
//     return request.post('/api/homework/submit', homework); // 提交作业的API地址，作业数据以JSON格式传递
// };
//
// // 上传作业文件接口
// export const uploadHomeworkFileService = (formData) => {
//     return request.post('/api/homework/upload', formData, {
//         headers: {
//             'Content-Type': 'multipart/form-data', // 设置文件上传的请求头
//         },
//     });
// };
// src/api/user.js

// 模拟获取作业列表的接口
export const userGetHomeworkListService = () => {
    return new Promise((resolve) => {
        setTimeout(() => {
            resolve([
                { title: '作业1: 阅读资料并回答问题', dueDate: '2024-10-15', status: '未提交' },
                { title: '作业2: 编写Vue组件', dueDate: '2024-10-18', status: '未提交' },
                { title: '作业3: 测试前端页面', dueDate: '2024-10-20', status: '未提交' }
            ]);
        }, 500); // 模拟网络延迟
    });
};

// 模拟提交作业的接口
export const userSubmitHomeworkService = (homework) => {
    return new Promise((resolve) => {
        setTimeout(() => {
            resolve({ success: true });
        }, 500); // 模拟网络延迟
    });
};
