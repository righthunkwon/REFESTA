import imageCompression from 'browser-image-compression';

const browserImageCompression = async (file) => {
  try {
    const options = {
      maxSizeMb: 1,
      maxWidthOrHeight: 1024,
    };
    return await imageCompression(file, options);
  } catch (error) {
    //error
  }
  return;
};

export default browserImageCompression;
