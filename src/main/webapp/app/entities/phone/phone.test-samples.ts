import dayjs from 'dayjs/esm';

import { IPhone, NewPhone } from './phone.model';

export const sampleWithRequiredData: IPhone = {
  id: 15845,
  name: 'Forward Berkshire Lake',
  brand: 'Optimization Plastic Product',
  device: 'Solutions indigo Specialist',
};

export const sampleWithPartialData: IPhone = {
  id: 89426,
  name: 'Zimbabwe Fresh',
  brand: 'digital standardization',
  device: 'deposit hard Australian',
  bookedOn: dayjs('2023-05-12T04:09'),
};

export const sampleWithFullData: IPhone = {
  id: 55393,
  name: 'District Alabama deposit',
  brand: 'COM disintermediate',
  device: 'Indiana',
  bookedOn: dayjs('2023-05-11T19:41'),
};

export const sampleWithNewData: NewPhone = {
  name: 'reboot',
  brand: 'withdrawal 1080p',
  device: 'hard Concrete',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
