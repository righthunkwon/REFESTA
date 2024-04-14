import { useNavigate } from 'react-router';

const FestivalHomeItem = ({ id, posterUrl, name, date }) => {
  const nav = useNavigate();

  const onClickItem = () => {
    nav(`/festival/${id}`);
  };

  const dateFormat = (festivalDate) => {
    return (
      festivalDate.getFullYear() +
      '.' +
      (festivalDate.getMonth() + 1 < 9 ? '0' + (festivalDate.getMonth() + 1) : festivalDate.getMonth() + 1) +
      '.' +
      (festivalDate.getDate() <= 9 ? '0' + festivalDate.getDate() : festivalDate.getDate())
    );
  };

  if (!name) {
    return (
      <div className='w-[120px] h-full'>
        <div className='motion-safe:animate-pulse w-[120px] h-[160px] rounded-md bg-zinc-100 mb-1' src={posterUrl} />
        <div className='w-full h-4 mb-1 rounded-md motion-safe:animate-pulse bg-zinc-100'></div>
        <div className='w-full h-4 mb-1 rounded-md motion-safe:animate-pulse bg-zinc-100'></div>
      </div>
    );
  }
  return (
    <div className='w-[120px]' onClick={onClickItem}>
      <img className='w-full h-[160px] rounded-md' src={posterUrl} />
      <div className='truncate' title={name}>
        {name}
      </div>
      <div className='text-sm text-zinc-500'>{dateFormat(new Date(date))}</div>
    </div>
  );
};
export default FestivalHomeItem;
