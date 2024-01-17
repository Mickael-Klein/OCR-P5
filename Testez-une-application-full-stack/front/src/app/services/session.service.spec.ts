import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { SessionService } from './session.service';
import { SessionInformation } from '../interfaces/sessionInformation.interface';

describe('SessionService', () => {
  let service: SessionService;

  const mockUser: SessionInformation = {
    token: 'token',
    type: 'type',
    id: 10,
    username: 'username',
    firstName: 'firstName',
    lastName: 'lastName',
    admin: false,
  };

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [SessionService],
    });
    service = TestBed.inject(SessionService);
  });

  test('should be created', () => {
    expect(service).toBeTruthy();
  });

  test('should initialize with isLogged as false and sessionInformation as undefined', () => {
    expect(service.isLogged).toBe(false);
    expect(service.sessionInformation).toBe(undefined);
  });

  test('should emit initial value of isLogged with $isLogged', () => {
    service.$isLogged().subscribe((isLogged) => {
      expect(isLogged).toBe(false);
    });
  });

  test('should log in user and update isLogged and sessionInformation', () => {
    service.logIn(mockUser);

    expect(service.isLogged).toBe(true);
    expect(service.sessionInformation).toEqual(mockUser);

    service.$isLogged().subscribe((isLogged) => {
      expect(isLogged).toBe(true);
    });
  });

  test('should log out user and update isLogged and sessionInformation', () => {
    service.logIn(mockUser);
    service.logOut();

    expect(service.isLogged).toBe(false);
    expect(service.sessionInformation).toBeUndefined();

    service.$isLogged().subscribe((isLogged) => {
      expect(isLogged).toBe(false);
    });
  });
});
