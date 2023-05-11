import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { PhoneFormService, PhoneFormGroup } from './phone-form.service';
import { IPhone } from '../phone.model';
import { PhoneService } from '../service/phone.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

@Component({
  selector: 'jhi-phone-update',
  templateUrl: './phone-update.component.html',
})
export class PhoneUpdateComponent implements OnInit {
  isSaving = false;
  phone: IPhone | null = null;

  usersSharedCollection: IUser[] = [];

  editForm: PhoneFormGroup = this.phoneFormService.createPhoneFormGroup();

  constructor(
    protected phoneService: PhoneService,
    protected phoneFormService: PhoneFormService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ phone }) => {
      this.phone = phone;
      if (phone) {
        this.updateForm(phone);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const phone = this.phoneFormService.getPhone(this.editForm);
    if (phone.id !== null) {
      this.subscribeToSaveResponse(this.phoneService.update(phone));
    } else {
      this.subscribeToSaveResponse(this.phoneService.create(phone));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPhone>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(phone: IPhone): void {
    this.phone = phone;
    this.phoneFormService.resetForm(this.editForm, phone);

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing<IUser>(this.usersSharedCollection, phone.bookedBy);
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing<IUser>(users, this.phone?.bookedBy)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }
}
