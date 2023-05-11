import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPhone } from '../phone.model';
import { PhoneService } from '../service/phone.service';

@Injectable({ providedIn: 'root' })
export class PhoneRoutingResolveService implements Resolve<IPhone | null> {
  constructor(protected service: PhoneService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPhone | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((phone: HttpResponse<IPhone>) => {
          if (phone.body) {
            return of(phone.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(null);
  }
}
