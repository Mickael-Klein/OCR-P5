import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { SessionService } from '../../../../services/session.service';

import { DetailComponent } from './detail.component';
import { SessionApiService } from '../../services/session-api.service';
import { TeacherService } from 'src/app/services/teacher.service';
import { ActivatedRoute, Router } from '@angular/router';
import { of } from 'rxjs';

describe('DetailComponent', () => {
  let component: DetailComponent;
  let fixture: ComponentFixture<DetailComponent>;
  let mockSessionService: any;
  let mockSessionApiService: any;
  let mockTeacherService: any;
  let mockMatSnackBar: any;
  let mockRouter: any;
  let mockActivatedRoute: any;
  let mockSessionInformationData: any;
  let mockTeacherData: any;

  beforeEach(async () => {
    mockActivatedRoute = {
      snapshot: {
        paramMap: {
          get: jest.fn().mockReturnValue('1'),
        },
      },
    };

    mockSessionService = {
      sessionInformation: {
        admin: true,
        id: 1,
      },
    };

    mockSessionInformationData = {
      id: 1,
      name: 'Goat Yoga',
      description: 'Will you handle it ?',
      date: new Date(),
      teacher_id: 10,
      users: [2, 3, 4],
      createdAt: new Date('2024-01-15'),
      updatedAt: new Date('2024-01-16'),
    };

    mockTeacherData = {
      id: 10,
      lastName: 'lastName',
      firstName: 'firstName',
      createdAt: new Date(),
      updatedAt: new Date(),
    };

    mockSessionApiService = {
      detail: jest.fn().mockReturnValue(of(mockSessionInformationData)),
      delete: jest.fn().mockReturnValue(of(undefined)),
      participate: jest.fn().mockImplementation(() => {
        mockSessionInformationData.users.push(1);
        return of(undefined);
      }),
      unParticipate: jest.fn().mockImplementation(() => {
        mockSessionInformationData.users.pop();
        return of(undefined);
      }),
    };

    mockTeacherService = {
      detail: jest.fn().mockReturnValue(of(mockTeacherData)),
    };

    mockMatSnackBar = { open: jest.fn() };

    mockRouter = { navigate: jest.fn() };

    window.history.back = jest.fn();

    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        HttpClientModule,
        MatSnackBarModule,
        ReactiveFormsModule,
      ],
      declarations: [DetailComponent],
      providers: [
        { provide: SessionService, useValue: mockSessionService },
        { provide: SessionApiService, useValue: mockSessionApiService },
        { provide: TeacherService, useValue: mockTeacherService },
        { provide: MatSnackBar, useValue: mockMatSnackBar },
        { provide: Router, useValue: mockRouter },
        { provide: ActivatedRoute, useValue: mockActivatedRoute },
      ],
    }).compileComponents();
    fixture = TestBed.createComponent(DetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  test('should create', () => {
    expect(component).toBeTruthy();
  });

  test('should call fetchSession onInit, call SessionApiService to hydrate session, isParticipate and call teacherService to hydrate teacher', () => {
    //@ts-ignore
    const fetchSessionSpy = jest.spyOn(component, 'fetchSession');

    component.ngOnInit();
    expect(fetchSessionSpy).toHaveBeenCalled();
    expect(mockActivatedRoute.snapshot.paramMap.get).toHaveBeenCalled();
    expect(mockSessionApiService.detail).toHaveBeenCalledWith('1');
    expect(component.sessionId).toEqual('1');
    expect(component.session).toEqual(mockSessionInformationData);
    expect(component.isParticipate).toBe(false);
    expect(mockTeacherService.detail).toHaveBeenCalledWith('10');
    expect(component.teacher).toEqual(mockTeacherData);
  });

  test('should set isParticipate to true onInit if userId is in session user array', () => {
    mockSessionInformationData.users.push(1);
    component.ngOnInit();
    expect(component.isParticipate).toBe(true);
  });

  test('should go back when Back() is called', () => {
    component.back();
    expect(window.history.back).toHaveBeenCalledTimes(1);
  });

  test('should on delete, call sessionApiService.delete, pop up snackbar and redirect user', () => {
    component.delete();
    expect(mockSessionApiService.delete).toHaveBeenCalled();
    expect(mockMatSnackBar.open).toHaveBeenCalledWith(
      'Session deleted !',
      'Close',
      { duration: 3000 }
    );
    expect(mockRouter.navigate).toHaveBeenCalledWith(['sessions']);
  });

  test('should on participate, call sessionApiService.participate, then call fetchSession method, final: isParticipate should be true', () => {
    //@ts-ignore
    const fetchSessionSpy = jest.spyOn(component, 'fetchSession');

    expect(component.isParticipate).toBe(false);

    component.participate();
    expect(mockSessionApiService.participate).toHaveBeenCalledWith('1', '1');

    expect(fetchSessionSpy).toHaveBeenCalled();
    expect(component.isParticipate).toBe(true);
  });

  test('should on unParticipate, call sessionApiService.unParticipate, then call fetchSession method, final: isParticipate should be false', () => {
    //@ts-ignore
    const fetchSessionSpy = jest.spyOn(component, 'fetchSession');

    mockSessionInformationData.users.push(1);
    component.ngOnInit();
    expect(component.isParticipate).toBe(true);

    component.unParticipate();

    expect(mockSessionApiService.unParticipate).toHaveBeenCalledWith('1', '1');
    expect(fetchSessionSpy).toHaveBeenCalled();
    expect(component.isParticipate).toBe(false);
  });
});
