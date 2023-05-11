import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { PhoneComponent } from './list/phone.component';
import { PhoneDetailComponent } from './detail/phone-detail.component';
import { PhoneUpdateComponent } from './update/phone-update.component';
import { PhoneDeleteDialogComponent } from './delete/phone-delete-dialog.component';
import { PhoneRoutingModule } from './route/phone-routing.module';

@NgModule({
  imports: [SharedModule, PhoneRoutingModule],
  declarations: [PhoneComponent, PhoneDetailComponent, PhoneUpdateComponent, PhoneDeleteDialogComponent],
})
export class PhoneModule {}
