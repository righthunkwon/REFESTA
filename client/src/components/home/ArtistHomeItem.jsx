import { useNavigate } from 'react-router';

const ArtistHomeItem = ({ id, name, pictureUrl }) => {
  const nav = useNavigate();

  const onClickItem = () => {
    nav(`/artist/${id}`);
  };

  if (!name) {
    return (
      <div>
        <div className='w-[120px] h-[200px] '>
          <div className='w-[120px] motion-safe:animate-pulse bg-zinc-100 h-full rounded-full ' src={pictureUrl} />
        </div>
      </div>
    );
  }
  return (
    <div className='relative text-center' onClick={onClickItem}>
      <div className='absolute w-full h-full border border-zinc-200 rounded-full bg-gradient-to-t from-stone-800 to-25%'></div>
      <div className='w-[120px] h-[200px]'>
        <img className='object-cover w-full h-full rounded-full ' src={pictureUrl} />
      </div>
      <div className='absolute w-1/2 text-sm text-white truncate bottom-2 left-7' title={name}>
        {name}
      </div>
    </div>
  );
};
export default ArtistHomeItem;
