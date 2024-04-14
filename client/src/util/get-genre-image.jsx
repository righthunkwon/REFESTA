import rap_hiphop from '@assets/genre/rap_hiphop.png';
import indie from '@assets/genre/indie.png';
import rock_metal from '@assets/genre/rock_metal.png';
import electronica from '@assets/genre/electronica.png';
import jazz from '@assets/genre/jazz.png';
import rb_soul from '@assets/genre/rb_soul.png';
import balad from '@assets/genre/balad.png';
import dance from '@assets/genre/dance.png';
import forks_blues from '@assets/genre/forks_blues.png';
// 이미지 번호를 기준으로 반환
export function getGenreImage(genreName) {
  switch (genreName) {
    case 'rap_hiphop':
      return rap_hiphop;
    case 'indie':
      return indie;
    case 'rock_metal':
      return rock_metal;
    case 'electronica':
      return electronica;
    case 'jazz':
      return jazz;
    case 'rb_soul':
      return rb_soul;
    case 'balad':
      return balad;
    case 'dance':
      return dance;
    case 'forks_blues':
      return forks_blues;
    default:
      return null;
  }
}
