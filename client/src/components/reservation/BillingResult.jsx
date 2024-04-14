const BillingResult = ({ billingResult }) => {
  // 천단위 콤마 넣기
  const addComma = (price) => {
    let returnString = price?.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',');
    return returnString;
  };

  return (
    <div className='w-11/12 mt-5'>
      <div className='w-full px-5 py-3 bg-gray-100 rounded-md'>
        <div className='py-1 text-xl font-bold border-b-2 border-gray-400'>결제 내역</div>
        <div className='px-2 py-2'>
          <div className='flex justify-between text-xs'>
            <div className='flex-none'>페스티별명</div>
            <div className='font-bold flex-2'>{billingResult.name}</div>
          </div>
          <div className='flex justify-between mt-2 text-xs'>
            <div className='flex-none'>일시</div>
            <div className='pl-3 font-bold text-right whitespace-normal'>{billingResult.festivalDate}</div>
          </div>
          <div className='flex justify-between mt-2 text-xs'>
            <div className='flex-none'>장소</div>
            <div className='pl-3 font-bold text-right whitespace-normal'>{billingResult.location}</div>
          </div>
          <div className='flex justify-between mt-2 text-xs'>
            <div className='flex-none'>결제일시</div>
            <div className='pl-3 font-bold text-right whitespace-normal'>
              {new Date(billingResult.paymentDate).getFullYear()}-
              {new Date(billingResult.paymentDate).getMonth() < 9
                ? `0${new Date(billingResult.paymentDate).getMonth() + 1}`
                : new Date(billingResult.paymentDate).getMonth() + 1}
              -
              {new Date(billingResult.paymentDate).getDate() < 10
                ? `0${new Date(billingResult.paymentDate).getDate()}`
                : new Date(billingResult.paymentDate).getDate()}
            </div>
          </div>
          <div className='flex justify-between mt-2 text-xs'>
            <div className='flex-none'>티켓 수</div>
            <div className='pl-3 font-bold text-right whitespace-normal'>{billingResult.count}</div>
          </div>
        </div>
      </div>
      <div className='pr-2 mt-3 text-3xl font-bold text-right'>{addComma(billingResult.price)}원</div>
    </div>
  );
};

export default BillingResult;
