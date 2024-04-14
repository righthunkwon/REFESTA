import img404 from '@assets/404img.png';
import { Link } from 'react-router-dom';

const NotfoundPage = () => {
  return (
    <div className='flex flex-col items-center justify-center h-full text-center gap-y-7 px-7'>
      <div>
        <img src={img404} />
      </div>
      <div className='text-xs'>
        <h2>페이지를 찾을 수 없습니다.</h2>
        <h2>존재하지 않는 주소를 입력하셨거나</h2>
        <h2>요청하신 주소가 변경,삭제되어 찾을 수 없습니다.</h2>
      </div>
      <div className='grid items-center w-48 h-12 text-center text-white rounded-full cursor-pointer bg-ourIndigo hover:bg-ourPink hover:text-white'>
        <Link to='/'>Refesta 홈으로</Link>
      </div>
    </div>
  );
};

export default NotfoundPage;
