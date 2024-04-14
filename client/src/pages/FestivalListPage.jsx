import { useNavigate, useParams } from 'react-router';

import FestivalViewAllList from '@components/home/FestivalViewAllList';

const FestivalListPage = () => {
  const { state } = useParams();
  return (
    <div>
      {state === 'scheduled' ? (
        <FestivalViewAllList title={'예정'} state={state} />
      ) : state === 'ended' ? (
        <FestivalViewAllList title={'지난'} state={state} />
      ) : (
        window.location.replace('/404')
      )}
    </div>
  );
};

export default FestivalListPage;
