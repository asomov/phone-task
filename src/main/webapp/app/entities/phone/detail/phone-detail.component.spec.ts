import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PhoneDetailComponent } from './phone-detail.component';

describe('Phone Management Detail Component', () => {
  let comp: PhoneDetailComponent;
  let fixture: ComponentFixture<PhoneDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PhoneDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ phone: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(PhoneDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(PhoneDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load phone on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.phone).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
