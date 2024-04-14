import { Link, useNavigate } from 'react-router-dom';

import logo from '@assets/refesta_logo.png';
import glogo from '@assets/google_small_logo.png';

const Login = () => {
  const navigate = useNavigate();

  const handleNavigateBack = () => {
    navigate(-1);
  };

  const handleGoogleLogin = () => {
    // 구글 로그인 화면으로 이동시키기
    // 새창을 켜야할지도?
    window.location.href = `https://accounts.google.com/o/oauth2/v2/auth?
		client_id=${import.meta.env.VITE_GOOGLE_AUTH_CLIENT_ID}
		&redirect_uri=${import.meta.env.VITE_GOOGLE_AUTH_REDIRECT_URI}
		&response_type=code
		&scope=email profile https://www.googleapis.com/auth/youtube`;
  };

  return (
    <div>
      <img src={logo} />
      <h3 className='text-2xl font-bold leading-9 tracking-tight text-center text-ourIndigo'>당신만을 위한 페스티벌</h3>
      <h2 className='text-3xl font-bold leading-9 tracking-tight text-center text-ourIndigo'>Refesta</h2>
      <div
        className='flex items-center justify-center w-full font-semibold bg-white shadow-md cursor-pointer shadow-zinc-400 h-14 mt-14'
        onClick={handleGoogleLogin}
      >
        <img className='mr-4 max-w-4' src={glogo} />
        <button>Google로 시작하기</button>
      </div>
      <div className='text-xs text-zinc-300 mt-7'>
        Refesta는 Google API에서 받은 정보를 사용하고 다른 앱으로 전송하는 것은 제한된 사용 요구 사항을 포함하여{' '}
        <Link
          className='underline text-zinc-400'
          to='https://developers.google.com/terms/api-services-user-data-policy'
        >
          Google API Services User Data Policy
        </Link>
        을 준수합니다.
      </div>
    </div>
  );
};
export default Login;
