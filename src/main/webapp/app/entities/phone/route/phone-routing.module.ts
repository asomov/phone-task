import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PhoneComponent } from '../list/phone.component';
import { PhoneDetailComponent } from '../detail/phone-detail.component';
import { PhoneUpdateComponent } from '../update/phone-update.component';
import { PhoneRoutingResolveService } from './phone-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const phoneRoute: Routes = [
  {
    path: '',
    component: PhoneComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PhoneDetailComponent,
    resolve: {
      phone: PhoneRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PhoneUpdateComponent,
    resolve: {
      phone: PhoneRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PhoneUpdateComponent,
    resolve: {
      phone: PhoneRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(phoneRoute)],
  exports: [RouterModule],
})
export class PhoneRoutingModule {}
