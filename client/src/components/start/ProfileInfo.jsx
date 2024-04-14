import { useEffect, useRef, useState } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';

import Swal from 'sweetalert2';

import defaultImg from '@assets/default_img.jpg';
import editPencil from '@assets/edit_pencil.png';

import { useProfileQuery, usePostProfileQuery } from '@/queries/startPagesQueries';

const ProfileInfo = ({ setStep, stepParam }) => {
  const nav = useNavigate();
  const { state } = useLocation();

  const { data, isLoading, isError, error } = useProfileQuery();
  const [nickname, setNickname] = useState('');
  const [imgInfo, setImgInfo] = useState({
    url: '',
    file: null,
  });

  const { data: postData, isLoading: isPostLoading, mutate } = usePostProfileQuery();

  // 사용자 정보 설정
  const getUserProfile = () => {
    if (!isError) {
      const nickname = data.data.data.nickname;
      const url = data.data.data.profileUrl;
      setNickname(nickname ? nickname : '');
      setImgInfo({ ...imgInfo, url: url });
    } else {
      window.location.replace('/login');
    }
  };

  useEffect(() => {
    if (!state) {
      nav('/', { replace: true });
    }
    if (!isLoading) getUserProfile();
  }, [isLoading]);

  // 닉네임 변경
  const onChangeNickName = (e) => {
    setNickname(e.target.value);
  };

  // 파일 변경
  const inputFile = useRef();
  // 연필을 눌렀을 때, 파일 인풋 누른것 처럼 동작
  const onClickInputFile = (e) => {
    e.preventDefault(); // 혹시 모르니 기존 동작 막고
    inputFile.current.click(); // 파일인풋 클릭
  };
  // 이미지 변경되었을 때, 미리보기
  const onChangeImgFile = (e) => {
    if (!e.target.value) return;
    // 파일 용량/확장자 체크하기
    let maxSize = 10 * 1024 * 1024; // 10mb
    const allowedTypes = ['image/jpeg', 'image/png', 'image/gif'];

    let type = e.target.files[0].type;
    let size = e.target.files[0].size;

    if (size > maxSize) {
      Swal.fire({
        title: '파일 용량 초과',
        html: '파일 용량이 너무 큽니다.<br>10MB 이하의 파일을 첨부해 주세요. ',
        confirmButtonColor: '#061E58',
        confirmButtonText: '확인',
      });
      return;
    }
    if (!allowedTypes.includes(type)) {
      Swal.fire({
        title: '지원하지 않는 타입',
        html: '지원하지 않는 파일 타입입니다.<br>jpg/jpeg/png/gif 중에서 첨부해 주세요.',
        confirmButtonColor: '#061E58',
        confirmButtonText: '확인',
      });
      return;
    }

    setImgInfo((preState) => {
      return {
        url: URL.createObjectURL(e.target.files[0]),
        file: e.target.files[0],
      };
    });
  };

  // 사용자 입력 정보 서버로 전달
  const onClickRegist = () => {
    if (!nickname) {
      Swal.fire({
        title: '닉네임 설정',
        html: '닉네임을 한 글자 이상 입력해 주세요.',
        confirmButtonColor: '#061E58',
        confirmButtonText: '확인',
      });
      return;
    }
    const formData = new FormData();
    formData.append('file', imgInfo.file);
    formData.append('nickname', nickname);

    mutate(formData);

    if (!isLoading) {
      setStep(stepParam.step2);
    }
    if (isError) {
      Swal.fire({
        title: '요청 실패',
        html: '가입에 실패했습니다.<br>다시 시도해 주세요.',
        confirmButtonColor: '#061E58',
        confirmButtonText: '확인',
      });
      //error
    }
  };

  return (
    <div className='grid gap-y-5'>
      <div className='text-2xl font-bold leading-9 tracking-tight text-center text-ourIndigo'>프로필 설정하기</div>
      <div className='relative w-full'>
        <form>
          <input type='file' accept='image/*' ref={inputFile} className='hidden' onChange={onChangeImgFile} />
          <img
            className='object-cover w-full border rounded-full aspect-square border-zinc-300'
            src={imgInfo.url ? imgInfo.url : defaultImg}
          />
          <div
            className='absolute bottom-7 right-3 overflow-hidden flex justify-center bg-[#D9D9D9] rounded-full w-10 h-10 cursor-pointer'
            onClick={onClickInputFile}
          >
            <img className='object-contain w-1/2 h-full' src={editPencil} />
          </div>
        </form>
      </div>
      <input
        className='flex items-center justify-center w-full pl-5 rounded-md bg-ourBrightGray h-14'
        type='text'
        placeholder='닉네임 입력'
        value={nickname}
        onChange={onChangeNickName}
      />
      <button
        className='flex items-center justify-center w-full text-white rounded-md bg-ourIndigo h-14'
        onClick={onClickRegist}
      >
        설정 완료
      </button>
    </div>
  );
};
export default ProfileInfo;
