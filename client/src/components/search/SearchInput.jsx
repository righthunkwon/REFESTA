import { useState, useEffect, useRef } from 'react';
import { Link, useNavigate, useSearchParams } from 'react-router-dom';
import useSearchStore from '@store/searchStore';

import instance from '@util/token_interceptor';

import backspace from '@assets/backspace.png';
import x_btn from '@assets/x_btn.png';

// 검색어 입력 인풋
const SearchInput = () => {
  const { searchKeyword, changeSearchKeyword, setAutoCompleteList, autoCompleteList } = useSearchStore();
  const [searchParams] = useSearchParams();
  const [urlSearchKeyword, setUrlSearchKeyword] = useState('');
  const nav = useNavigate();
  const inputDiv = useRef();

  // 검색어의 쿼리 파라미터가 변경되었을 때
  // 해당 단어를 가져와 검색어 창에 반영하는 함수 실행
  useEffect(() => {
    const urlKeyword = searchParams.get('word') || '';
    setUrlSearchKeyword(urlKeyword);
  }, [searchParams]);

  const onFocusInput = () => {
    if (searchKeyword) nav('/search');
  };

  const onChangeInput = (e) => {
    changeSearchKeyword(e.target.value);
  };

  const includeSearchKeywordInAutocomplete = () => {
    let result = {};
    const festivalKeyword = autoCompleteList.festivalWordList;
    const artistKeyword = autoCompleteList.artistWordList;
    festivalKeyword.map((keyword) => {
      if (keyword.name === searchKeyword) {
        result = { ...keyword, classification: 'festivals' };
      }
    });
    artistKeyword.map((keyword) => {
      if (keyword.name === searchKeyword) {
        result = { ...keyword, classification: 'artists' };
      }
    });
    return result;
  };

  const onKeyUpInput = (e) => {
    // 뒤로가기 키보드 버튼(Backspace)이 눌렸을 때
    // 검색어를 초기화하는 함수
    if (e.keyCode === 8) {
      setUrlSearchKeyword('');
    }

    // 엔터 누르면 검색 결과 페이지로
    if (e.key === 'Enter' && searchKeyword) {
      inputDiv.current.blur();

      // 자동완성 리스트에 검색어와 완전 일치하는 경우가 있어도 선호도 반영
      const searchKeywordByAutocomplete = includeSearchKeywordInAutocomplete();
      if (searchKeyword && !searchKeywordByAutocomplete) {
        const response = instance.patch(
          `recommendations/${searchKeywordByAutocomplete.classification}/${searchKeywordByAutocomplete.id}?point=5`
        );
      }

      nav(`/search/result?word=${searchKeyword}`);
    }

    // 자동 완성 검색어 요청
    if (searchKeyword) getAutoComplete();
  };

  const getAutoComplete = async () => {
    const response = await instance.get(`searches?word=${searchKeyword}`);
    if (response.data.status === 'success') {
      setAutoCompleteList(response.data.data);
    }
  };

  const onClickXBtn = () => {
    changeSearchKeyword('');
  };

  useEffect(() => {
    inputDiv.current.focus();
    return changeSearchKeyword('');
  }, []);

  return (
    <header className='h-[70px] py-4 bg-ourIndigo'>
      <div className='flex items-center h-full'>
        {/* 뒤로 가기 버튼 */}
        <div className='w-5 h-5 ml-5'>
          <Link to='/' className='cursor-pointer'>
            <img src={backspace} alt='Logo' />
          </Link>
        </div>
        {/* 검색 인풋 */}
        <div className='relative w-full h-full px-5'>
          <input
            type='text'
            value={urlSearchKeyword || searchKeyword}
            ref={inputDiv}
            onFocus={onFocusInput}
            onChange={onChangeInput}
            onKeyUp={onKeyUpInput}
            placeholder='검색어를 입력하세요'
            className='bg-[#102B6A] w-full h-full rounded-full text-white pl-5 pr-8 focus:outline-none'
          />
          <img className='absolute bottom-3 right-8' src={x_btn} onClick={onClickXBtn} />
        </div>
      </div>
    </header>
  );
};
export default SearchInput;
