import { useEffect } from 'react';
import { useSearchParams, useNavigate } from 'react-router-dom';

import axios from 'axios';

import loading from '../../assets/loading.png';

const Google_Login = () => {
  const nav = useNavigate();

  const handleHome = () => {
    nav('/', { replace: true });
    window.location.reload();
  };

  const handleProfile = (data) => {
    nav('/regist', { state: { ...data }, replace: true });
    window.location.reload();
  };

  const [params, setParams] = useSearchParams();
  const code = params.get('code');
  const baseURL = `${import.meta.env.VITE_PUBLIC_API_SERVER}/login/oauth2/code/google`;

  const postLogin = async (code) => {
    const headers = {
      'Content-Type': 'text/plain;charset=utf-8',
    };

    try {
      const response = await axios.post(baseURL, code, {
        headers: headers,
      });

      // 토큰 저장
      localStorage.setItem('accessToken', response.data.data.accessToken);
      localStorage.setItem('refreshToken', response.data.data.refreshToken);

      // isSigUp으로 기존/신규 여부 판단
      response.data.data.signUp ? handleProfile(response.data.data) : handleHome();
    } catch (error) {
      //error
    }
  };

  useEffect(() => {
    if (code) {
      postLogin(code);
    }
  }, [code, nav]);

  return (
    <div className='flex flex-col items-center px-10 mx-0 my-auto'>
      <div className='w-1/5 '>
        <img className='w-full mr-3 motion-safe:animate-spin' src={loading} />
      </div>
      <div className='my-4 text-center '>정보 가져오는 중</div>
    </div>
  );
};

export default Google_Login;
