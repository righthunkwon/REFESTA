import { Link } from 'react-router-dom';

import text_logo from '@assets/text_logo.png';
import image1 from '@assets/about/image1.gif';
import image2 from '@assets/about/image2.png';
import image3 from '@assets/about/image3.png';
import image4 from '@assets/about/image4.png';
import image5 from '@assets/about/image5.png';
import image6 from '@assets/about/image6.png';
import image7 from '@assets/about/image7.png';
import image8 from '@assets/about/image8.png';

const AboutPage = () => {
  return (
    <div className='grid items-center justify-center w-full h-full gap-y-2 bg-zinc-100'>
      <div className='content-center w-full py-3 mt-3 '>
        <div className='flex justify-center '>
          <img className='h-28' src={text_logo} />
        </div>
      </div>
      <div className='flex w-full gap-4 p-4 text-white bg-ourIndigo'>
        <div className=''>
          <div className='mb-2 text-2xl'>
            나를 위한 <br />
            페스티벌, <br />
            나의 페스티벌
          </div>
          <div>가고싶은 페스티벌을 찾고, 다녀온 페스티벌을 되돌아보세요.</div>
        </div>
        <div>
          <div>
            <img className='rounded-md ' src={image1} />
          </div>
        </div>
      </div>

      <div className='flex gap-4 p-4 bg-white'>
        <div>
          <div>
            <img className='border rounded-md shadow-md border-zinc-100' src={image2} />
          </div>
        </div>
        <div>
          <div className='text-lg'>무엇을 좋아하시나요?</div>
          <div className='text-sm text-zinc-400'>
            유튜브 재생목록 연동 또는 선호하는 장르를 알려주세요! 당신만을 위한 추천 데이터로 사용됩니다.{' '}
          </div>
        </div>
      </div>
      <div className='flex gap-4 p-4 bg-white'>
        <div>
          <div className='text-lg'>나를 위한 페스티벌, 아티스트, 셋리스트</div>
          <div className='text-sm text-zinc-400'>
            내가 좋아할 페스티벌부터, 아티스트가 참여한 페스티벌, 지난 페스티벌까지 살펴볼 수 있습니다.
          </div>
        </div>
        <div>
          <div>
            <img className='border rounded-md shadow-md border-zinc-100' src={image4} />
          </div>
        </div>
      </div>
      <div className='flex gap-4 p-4 bg-white'>
        <div>
          <div className='w-'>
            <img className='border rounded-md shadow-md border-zinc-100' src={image6} />
          </div>
        </div>
        <div>
          <div className='text-lg'>예매하기</div>
          <div className='text-sm text-zinc-400'>
            곧 시작될 페스티벌을 놓치지 마세요! 페스티벌 정보를 확인하고 예매까지 할 수 있습니다.
          </div>
        </div>
      </div>
      <div className='flex gap-4 p-4 bg-white'>
        <div>
          <div className='text-lg'>다시 즐기기</div>
          <div className='text-sm text-zinc-400'>
            지나간 페스티벌이 아쉬우신가요? 페스티벌에서 아티스트가 불렀던 노래를 들으며 후기를 살펴보세요.
          </div>
        </div>
        <div>
          <div className='w-'>
            <img className='border rounded-md shadow-md border-zinc-100' src={image7} />
          </div>
        </div>
      </div>
      <div className='flex gap-4 p-4 mb-3 bg-white'>
        <div>
          <div className='w-'>
            <img className='border rounded-md shadow-md border-zinc-100' src={image8} />
          </div>
        </div>
        <div>
          <div className='text-lg'>나의 페스티벌</div>
          <div className='text-sm text-zinc-400'>
            페스티벌에서의 추억을 공유하고, 마이페이지에서 한 번에 관리하세요. 좋아하는 페스티벌과 아티스트 목록도
            확인할 수 있습니다.
          </div>
        </div>
      </div>
      <div className='mb-7'>
        <div className='flex justify-center w-full gap-4 '>
          <div className='grid items-center text-sm text-center text-white rounded-full cursor-pointer w-44 h-11 bg-ourIndigo hover:bg-ourPink hover:text-white'>
            <Link to='/login'>Refesta 시작하기</Link>
          </div>
        </div>
      </div>

      <div className='grid justify-center w-full gap-4 text-center mb-14 text-zinc-400'>
        <div>
          <Link to='/privacy_policy' className='text-sm underline'>
            개인정보처리방침
          </Link>
        </div>
        <div className='text-xs'>권정훈, 강지헌, 이경배, 이지원, 최정윤, 하재률</div>
      </div>
    </div>
  );
};

export default AboutPage;
