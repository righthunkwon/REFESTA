import { useState } from 'react';

import SetListWrapper from '@components/festivalDetail/SetListWrapper';
import ReviewWrapper from '@components/festivalDetail/ReviewWrapper';

// 완료된 페스티벌 탭(셋리스트/후기게시판)
const FestivalInfoContainer = () => {
  const [openInfoTab, setOpenInfoTab] = useState(1);

  return (
    <div className='pt-4 '>
      <ul className='flex h-full'>
        {/* 셋리스트 탭 */}
        <li className={`w-1/2 text-center ${openInfoTab === 1 ? 'border-t-2 border-ourIndigo' : ''}`}>
          <button
            className={'w-full h-full text-xs px-2 py-2'}
            onClick={() => {
              setOpenInfoTab(1);
            }}
          >
            <span className={`${openInfoTab === 1 ? 'text-ourIndigo font-bold' : 'text-gray-400 font-bold'}`}>
              셋리스트
            </span>
          </button>
        </li>
        {/* 후기게시판 탭 */}
        <li className={`w-1/2 text-center ${openInfoTab === 2 ? 'border-t-2 border-ourIndigo' : ''}`}>
          <button
            className={'w-full h-full text-xs px-5 py-2'}
            onClick={() => {
              setOpenInfoTab(2);
            }}
          >
            <span className={`${openInfoTab === 2 ? 'text-ourIndigo font-bold' : 'text-gray-400 font-bold'}`}>
              후기리스트
            </span>
          </button>
        </li>
      </ul>
      <div className='flex flex-col h-full min-w-0 text-center break-words'>
        <div className='flex-auto'>
          <div className={openInfoTab === 1 ? 'block' : 'hidden'}>
            <SetListWrapper />
          </div>
          <div className={openInfoTab === 2 ? 'block' : 'hidden'}>
            <ReviewWrapper />
          </div>
        </div>
      </div>
    </div>
  );
};
export default FestivalInfoContainer;
