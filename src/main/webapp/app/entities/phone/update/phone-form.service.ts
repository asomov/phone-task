import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IPhone, NewPhone } from '../phone.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPhone for edit and NewPhoneFormGroupInput for create.
 */
type PhoneFormGroupInput = IPhone | PartialWithRequiredKeyOf<NewPhone>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IPhone | NewPhone> = Omit<T, 'bookedOn'> & {
  bookedOn?: string | null;
};

type PhoneFormRawValue = FormValueOf<IPhone>;

type NewPhoneFormRawValue = FormValueOf<NewPhone>;

type PhoneFormDefaults = Pick<NewPhone, 'id' | 'bookedOn'>;

type PhoneFormGroupContent = {
  id: FormControl<PhoneFormRawValue['id'] | NewPhone['id']>;
  name: FormControl<PhoneFormRawValue['name']>;
  brand: FormControl<PhoneFormRawValue['brand']>;
  device: FormControl<PhoneFormRawValue['device']>;
  bookedOn: FormControl<PhoneFormRawValue['bookedOn']>;
  bookedBy: FormControl<PhoneFormRawValue['bookedBy']>;
};

export type PhoneFormGroup = FormGroup<PhoneFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class PhoneFormService {
  createPhoneFormGroup(phone: PhoneFormGroupInput = { id: null }): PhoneFormGroup {
    const phoneRawValue = this.convertPhoneToPhoneRawValue({
      ...this.getFormDefaults(),
      ...phone,
    });
    return new FormGroup<PhoneFormGroupContent>({
      id: new FormControl(
        { value: phoneRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      name: new FormControl(phoneRawValue.name, {
        validators: [Validators.required],
      }),
      brand: new FormControl(phoneRawValue.brand, {
        validators: [Validators.required],
      }),
      device: new FormControl(phoneRawValue.device, {
        validators: [Validators.required],
      }),
      bookedOn: new FormControl(phoneRawValue.bookedOn),
      bookedBy: new FormControl(phoneRawValue.bookedBy),
    });
  }

  getPhone(form: PhoneFormGroup): IPhone | NewPhone {
    return this.convertPhoneRawValueToPhone(form.getRawValue() as PhoneFormRawValue | NewPhoneFormRawValue);
  }

  resetForm(form: PhoneFormGroup, phone: PhoneFormGroupInput): void {
    const phoneRawValue = this.convertPhoneToPhoneRawValue({ ...this.getFormDefaults(), ...phone });
    form.reset(
      {
        ...phoneRawValue,
        id: { value: phoneRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): PhoneFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      bookedOn: currentTime,
    };
  }

  private convertPhoneRawValueToPhone(rawPhone: PhoneFormRawValue | NewPhoneFormRawValue): IPhone | NewPhone {
    return {
      ...rawPhone,
      bookedOn: dayjs(rawPhone.bookedOn, DATE_TIME_FORMAT),
    };
  }

  private convertPhoneToPhoneRawValue(
    phone: IPhone | (Partial<NewPhone> & PhoneFormDefaults)
  ): PhoneFormRawValue | PartialWithRequiredKeyOf<NewPhoneFormRawValue> {
    return {
      ...phone,
      bookedOn: phone.bookedOn ? phone.bookedOn.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
