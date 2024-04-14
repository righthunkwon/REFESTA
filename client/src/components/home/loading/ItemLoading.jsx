import ArtistHomeItem from '../ArtistHomeItem';
import FestivalHomeItem from '../FestivalHomeItem';
import SetListHomeColItem from '../SetListHomeColItem';

const dummydata = [{ id: 1 }, { id: 2 }];
const ItemLoading = ({ type }) => {
  if (type === 'setList') {
    return (
      <>
        <SetListHomeColItem chunkItem={dummydata} />
        <SetListHomeColItem chunkItem={dummydata} />
      </>
    );
  } else if (type === 'festival') {
    return (
      <>
        {dummydata.map((item) => (
          <FestivalHomeItem key={item.id} />
        ))}
        {dummydata.map((item) => (
          <FestivalHomeItem key={item.id} />
        ))}
      </>
    );
  } else if (type === 'artist') {
    return (
      <>
        {dummydata.map((item) => (
          <ArtistHomeItem key={item.id} />
        ))}
        {dummydata.map((item) => (
          <ArtistHomeItem key={item.id} />
        ))}
      </>
    );
  }
  return <></>;
};

export default ItemLoading;
