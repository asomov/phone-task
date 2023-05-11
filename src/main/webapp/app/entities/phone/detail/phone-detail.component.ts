import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPhone } from '../phone.model';

@Component({
  selector: 'jhi-phone-detail',
  templateUrl: './phone-detail.component.html',
})
export class PhoneDetailComponent implements OnInit {
  phone: IPhone | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ phone }) => {
      this.phone = phone;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
