import dayjs from 'dayjs/esm';
import { IUser } from 'app/entities/user/user.model';

export interface IPhone {
  id: number;
  name?: string | null;
  brand?: string | null;
  device?: string | null;
  bookedOn?: dayjs.Dayjs | null;
  bookedBy?: Pick<IUser, 'id'> | null;
}

export type NewPhone = Omit<IPhone, 'id'> & { id: null };
