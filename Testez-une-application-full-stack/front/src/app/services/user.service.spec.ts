import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { UserService } from './user.service';
import {
  HttpClientTestingModule,
  HttpTestingController,
} from '@angular/common/http/testing';
import { User } from '../interfaces/user.interface';

describe('UserService', () => {
  let service: UserService;
  let httpTestingController: HttpTestingController;

  const mockUser: User = {
    id: 1,
    email: 'email',
    lastName: 'lastName',
    firstName: 'firstName',
    admin: false,
    password: 'password',
    createdAt: new Date(),
    updatedAt: new Date(),
  };

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [UserService],
    });
    service = TestBed.inject(UserService);
    httpTestingController = TestBed.inject(HttpTestingController);
  });

  test('should be created', () => {
    expect(service).toBeTruthy();
  });

  test('should on "getById", retrieve user from API via GET', () => {
    service.getById('1').subscribe((user) => {
      expect(user).toBe(mockUser);
    });

    const req = httpTestingController.expectOne('api/user/1');
    expect(req.request.method).toBe('GET');

    req.flush(mockUser);
  });

  test('should on "delete", delete user via DELETE', () => {
    service.delete('1').subscribe();

    const req = httpTestingController.expectOne('api/user/1');
    expect(req.request.method).toBe('DELETE');

    req.flush({});
  });
});
