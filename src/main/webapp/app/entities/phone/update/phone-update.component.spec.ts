import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { PhoneFormService } from './phone-form.service';
import { PhoneService } from '../service/phone.service';
import { IPhone } from '../phone.model';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

import { PhoneUpdateComponent } from './phone-update.component';

describe('Phone Management Update Component', () => {
  let comp: PhoneUpdateComponent;
  let fixture: ComponentFixture<PhoneUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let phoneFormService: PhoneFormService;
  let phoneService: PhoneService;
  let userService: UserService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [PhoneUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(PhoneUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PhoneUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    phoneFormService = TestBed.inject(PhoneFormService);
    phoneService = TestBed.inject(PhoneService);
    userService = TestBed.inject(UserService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call User query and add missing value', () => {
      const phone: IPhone = { id: 456 };
      const bookedBy: IUser = { id: 68673 };
      phone.bookedBy = bookedBy;

      const userCollection: IUser[] = [{ id: 83006 }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [bookedBy];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ phone });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(
        userCollection,
        ...additionalUsers.map(expect.objectContaining)
      );
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const phone: IPhone = { id: 456 };
      const bookedBy: IUser = { id: 7354 };
      phone.bookedBy = bookedBy;

      activatedRoute.data = of({ phone });
      comp.ngOnInit();

      expect(comp.usersSharedCollection).toContain(bookedBy);
      expect(comp.phone).toEqual(phone);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPhone>>();
      const phone = { id: 123 };
      jest.spyOn(phoneFormService, 'getPhone').mockReturnValue(phone);
      jest.spyOn(phoneService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ phone });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: phone }));
      saveSubject.complete();

      // THEN
      expect(phoneFormService.getPhone).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(phoneService.update).toHaveBeenCalledWith(expect.objectContaining(phone));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPhone>>();
      const phone = { id: 123 };
      jest.spyOn(phoneFormService, 'getPhone').mockReturnValue({ id: null });
      jest.spyOn(phoneService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ phone: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: phone }));
      saveSubject.complete();

      // THEN
      expect(phoneFormService.getPhone).toHaveBeenCalled();
      expect(phoneService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPhone>>();
      const phone = { id: 123 };
      jest.spyOn(phoneService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ phone });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(phoneService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareUser', () => {
      it('Should forward to userService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(userService, 'compareUser');
        comp.compareUser(entity, entity2);
        expect(userService.compareUser).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
