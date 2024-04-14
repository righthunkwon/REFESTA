import SetListHomeItem from '@components/home/SetListHomeItem';

const SetListHomeColItem = ({ chunkItem }) => {
  return (
    <div className='grid gap-y-5'>
      {chunkItem.map((item) => (
        <SetListHomeItem key={item.id} {...item} />
      ))}
    </div>
  );
};
export default SetListHomeColItem;
