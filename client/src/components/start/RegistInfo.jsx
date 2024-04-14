import { useState } from 'react';

import ProfileInfo from '@components/start/ProfileInfo';
import YoutubeConnect from '@components/start/YoutubeConnect';
import GenreList from '@components/start/GenreList';

// STEP : profile > youtube > genre
const stepParam = {
  step1: 'profile',
  step2: 'youtube',
  step3: 'genre',
};

const RegisterInfo = () => {
  const [steps, setStep] = useState(stepParam.step1);

  return (
    <div>
      {steps === stepParam.step1 ? (
        <ProfileInfo setStep={setStep} stepParam={stepParam} />
      ) : steps === stepParam.step2 ? (
        <YoutubeConnect setStep={setStep} stepParam={stepParam} />
      ) : steps === stepParam.step3 ? (
        <GenreList setStep={setStep} stepParam={stepParam} />
      ) : null}
    </div>
  );
};
export default RegisterInfo;
