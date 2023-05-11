import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPhone, NewPhone } from '../phone.model';

export type PartialUpdatePhone = Partial<IPhone> & Pick<IPhone, 'id'>;

type RestOf<T extends IPhone | NewPhone> = Omit<T, 'bookedOn'> & {
  bookedOn?: string | null;
};

export type RestPhone = RestOf<IPhone>;

export type NewRestPhone = RestOf<NewPhone>;

export type PartialUpdateRestPhone = RestOf<PartialUpdatePhone>;

export type EntityResponseType = HttpResponse<IPhone>;
export type EntityArrayResponseType = HttpResponse<IPhone[]>;

@Injectable({ providedIn: 'root' })
export class PhoneService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/phones');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(phone: NewPhone): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(phone);
    return this.http.post<RestPhone>(this.resourceUrl, copy, { observe: 'response' }).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(phone: IPhone): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(phone);
    return this.http
      .put<RestPhone>(`${this.resourceUrl}/${this.getPhoneIdentifier(phone)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(phone: PartialUpdatePhone): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(phone);
    return this.http
      .patch<RestPhone>(`${this.resourceUrl}/${this.getPhoneIdentifier(phone)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestPhone>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestPhone[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getPhoneIdentifier(phone: Pick<IPhone, 'id'>): number {
    return phone.id;
  }

  comparePhone(o1: Pick<IPhone, 'id'> | null, o2: Pick<IPhone, 'id'> | null): boolean {
    return o1 && o2 ? this.getPhoneIdentifier(o1) === this.getPhoneIdentifier(o2) : o1 === o2;
  }

  addPhoneToCollectionIfMissing<Type extends Pick<IPhone, 'id'>>(
    phoneCollection: Type[],
    ...phonesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const phones: Type[] = phonesToCheck.filter(isPresent);
    if (phones.length > 0) {
      const phoneCollectionIdentifiers = phoneCollection.map(phoneItem => this.getPhoneIdentifier(phoneItem)!);
      const phonesToAdd = phones.filter(phoneItem => {
        const phoneIdentifier = this.getPhoneIdentifier(phoneItem);
        if (phoneCollectionIdentifiers.includes(phoneIdentifier)) {
          return false;
        }
        phoneCollectionIdentifiers.push(phoneIdentifier);
        return true;
      });
      return [...phonesToAdd, ...phoneCollection];
    }
    return phoneCollection;
  }

  protected convertDateFromClient<T extends IPhone | NewPhone | PartialUpdatePhone>(phone: T): RestOf<T> {
    return {
      ...phone,
      bookedOn: phone.bookedOn?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restPhone: RestPhone): IPhone {
    return {
      ...restPhone,
      bookedOn: restPhone.bookedOn ? dayjs(restPhone.bookedOn) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestPhone>): HttpResponse<IPhone> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestPhone[]>): HttpResponse<IPhone[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
