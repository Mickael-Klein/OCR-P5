import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { SessionApiService } from './session-api.service';
import {
  HttpClientTestingModule,
  HttpTestingController,
} from '@angular/common/http/testing';

describe('SessionsService', () => {
  let service: SessionApiService;
  let httpTestingController: HttpTestingController;

  const mockSession = {
    id: 1,
    name: 'name',
    description: 'description',
    date: new Date(),
    teacher_id: 10,
    users: [2, 3, 4],
    createdAt: new Date(),
    updatedAt: new Date(),
  };
  const mockSessions = [mockSession, mockSession];

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    service = TestBed.inject(SessionApiService);
    httpTestingController = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpTestingController.verify();
  });

  test('should be created', () => {
    expect(service).toBeTruthy();
  });

  test('should on "all", retrieve sessions from the API via GET', () => {
    service.all().subscribe((sessions) => {
      expect(sessions).toEqual(mockSessions);
    });

    const req = httpTestingController.expectOne('api/session');
    expect(req.request.method).toBe('GET');

    req.flush(mockSessions);
  });

  test('should on "detail", retrieve session detail from the API via GET', () => {
    service.detail('1').subscribe((session) => {
      expect(session).toEqual(mockSession);
    });

    const req = httpTestingController.expectOne('api/session/1');
    expect(req.request.method).toBe('GET');

    req.flush(mockSession);
  });

  test('should on "delete", delete session via DELETE', () => {
    service.delete('1').subscribe();

    const req = httpTestingController.expectOne('api/session/1');
    expect(req.request.method).toBe('DELETE');

    req.flush({});
  });

  test('should on "create", create session via POST', () => {
    service.create(mockSession).subscribe((session) => {
      expect(session).toEqual(mockSession);
    });

    const req = httpTestingController.expectOne('api/session');
    expect(req.request.method).toBe('POST');

    req.flush(mockSession);
  });

  test('should on "update", update session via PUT', () => {
    service.update('1', mockSession).subscribe((session) => {
      expect(session).toEqual(mockSession);
    });

    const req = httpTestingController.expectOne('api/session/1');
    expect(req.request.method).toBe('PUT');

    req.flush(mockSession);
  });

  test('should on "participate", participate to a session via POST', () => {
    service.participate('1', '1').subscribe();

    const req = httpTestingController.expectOne('api/session/1/participate/1');
    expect(req.request.method).toBe('POST');

    req.flush({});
  });

  test('should on "unParticipate", unparticipate from a session via DELETE', () => {
    service.unParticipate('1', '1').subscribe();

    const req = httpTestingController.expectOne('api/session/1/participate/1');
    expect(req.request.method).toBe('DELETE');

    req.flush({});
  });
});
