import axios from 'axios';

let isAlertShown = false; // alert가 이미 떠있는지 여부를 저장하는 변수

// 헤더에 토큰이 들어간 axiosInstance
const instance = axios.create({
  baseURL: import.meta.env.VITE_PUBLIC_API_SERVER,
  headers: {
    'Access-Control-Allow-Origin': 'https://j10a601.p.ssafy.io/8081',
    'Access-Control-Allow-Credentials': 'true',
    'Access-Control-Allow-Methods': 'GET, POST, OPTIONS, PUT, PATCH, DELETE',
  },
});

// 토큰 만료시 재발급을 위한 axiosInstance
const refreshInstance = axios.create({
  baseURL: import.meta.env.VITE_PUBLIC_API_SERVER,
});

// 토큰 재발급 요청 - 리프레시 토큰은 바디에 담아서 보냄
const regenerateRefreshToken = async () => {
  const headers = {
    'Content-Type': 'text/plain;charset=utf-8',
  };
  const refreshToken = localStorage.getItem('refreshToken');

  const response = await refreshInstance.post('/login/oauth/token', refreshToken, { headers: headers });

  return response;
};

// 요청 인터셉터
instance.interceptors.request.use(
  (config) => {
    // 요청 전달 전 작업 수행 : 헤더에 토큰 넣기
    config.headers['Authorization'] = `Bearer ${localStorage.getItem('accessToken')}`;
    return config;
  },
  (error) => {
    // 요청 오류 시 작업 수행
    //error
    return Promise.reject(error);
  }
);

// 응답 인터셉터
instance.interceptors.response.use(
  (response) => {
    // 2xx 상태 코드 시 이 함수 트리거
    // 응답 데이터가 있는 작업 수행
    if (response.status == 404) {
      window.location.replace('/404');
    }
    return response;
  },
  async (error) => {
    // 2xx 이외 상태 코드 시 이 함수 트리거
    // 응답 오류가 있는 작업 수행
    if (error.response.status == 401) {
      if (!isAlertShown) {
        // 이미 alert가 띄워진 상태라면 다시 띄우지 않음
        isAlertShown = true; // alert가 떠있음을 표시
        const originRequest = error.config;
        //error
        try {
          const response = await regenerateRefreshToken();
          if (response.status == 200) {
            const newAccessToken = response.data.data.accessToken;
            localStorage.setItem('accessToken', response.data.data.accessToken);
            localStorage.setItem('refreshToken', response.data.data.refreshToken);
            axios.defaults.headers.common['Authorization'] = `Bearer ${newAccessToken}`;
            return axios(originRequest);
          }
        } catch (error) {
          // 리프레시 토큰으로 재발급이 안된거니까 로그인 다시!
          localStorage.removeItem('accessToken');
          localStorage.removeItem('refreshToken');
          window.location.replace('/login');
        } finally {
          isAlertShown = false; // alert 창이 닫히면 다시 false로 설정하여 다시 alert가 떠도록 함
        }
      }
    }
    return Promise.reject(error);
  }
);

export default instance;
