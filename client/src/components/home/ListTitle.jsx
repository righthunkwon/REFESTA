const ListTitle = ({ title, description, btn }) => {
  return (
    <div className='flex items-end mb-3 ml-4'>
      <div className='w-3/4'>
        <div className='text-lg'>{title}</div>
        <div className='text-sm text-zinc-400'>{description}</div>
      </div>
      <div className='w-1/4 pr-3 text-sm text-right text-zinc-500'>{btn}</div>
    </div>
  );
};
export default ListTitle;
