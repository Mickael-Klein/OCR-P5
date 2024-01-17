import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { SessionService } from 'src/app/services/session.service';
import { SessionApiService } from '../../services/session-api.service';

import { FormComponent } from './form.component';
import { ActivatedRoute, Router } from '@angular/router';
import { TeacherService } from 'src/app/services/teacher.service';
import { of } from 'rxjs';

describe('FormComponent', () => {
  let component: FormComponent;
  let fixture: ComponentFixture<FormComponent>;
  let mockActivatedRoute: any;
  let mockMatSnackBar: any;
  let mockSessionApiService: any;
  let mockTeacherService: any;
  let mockRouter: any;
  let mockSessionInformationData: any;
  let mockUpdateSessionInformationData: any;
  let mockTeacherData: any;

  const mockSessionService = {
    sessionInformation: {
      admin: true,
    },
  };

  beforeEach(async () => {
    mockActivatedRoute = {
      snapshot: {
        paramMap: {
          get: jest.fn().mockReturnValue('1'),
        },
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

    mockUpdateSessionInformationData = {
      name: 'New Name',
      description: 'New Description',
      date: new Date('2024-01-01'),
      teacher_id: 9,
    };

    mockTeacherData = [
      {
        id: 10,
        lastName: 'lastName',
        firstName: 'firstName',
        createdAt: new Date(),
        updatedAt: new Date(),
      },
      {
        id: 11,
        lastName: 'lastName11',
        firstName: 'firstName11',
        createdAt: new Date(),
        updatedAt: new Date(),
      },
      {
        id: 12,
        lastName: 'lastName12',
        firstName: 'firstName12',
        createdAt: new Date(),
        updatedAt: new Date(),
      },
    ];

    mockMatSnackBar = { open: jest.fn() };

    mockSessionApiService = {
      detail: jest.fn().mockReturnValue(of(mockSessionInformationData)),
      create: jest.fn().mockReturnValue(of(mockSessionInformationData)),
      update: jest.fn().mockImplementation(() => {
        mockSessionInformationData.description = 'A new way to practice';
        return of(mockSessionInformationData);
      }),
    };

    mockTeacherService = {
      all: jest.fn().mockReturnValue(of(mockTeacherData)),
    };

    mockRouter = {
      navigate: jest.fn(),
      url: '/create',
    };

    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        HttpClientModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule,
        MatSnackBarModule,
        MatSelectModule,
        BrowserAnimationsModule,
      ],
      providers: [
        { provide: SessionService, useValue: mockSessionService },
        { provide: ActivatedRoute, useValue: mockActivatedRoute },
        { provide: MatSnackBar, useValue: mockMatSnackBar },
        { provide: SessionApiService, useValue: mockSessionApiService },
        { provide: TeacherService, useValue: mockTeacherService },
        { provide: Router, useValue: mockRouter },
      ],
      declarations: [FormComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(FormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  test('should create', () => {
    expect(component).toBeTruthy();
  });

  test('should onInit, check if user isAdmin and redirect if not', () => {
    mockSessionService.sessionInformation.admin = false;
    component.ngOnInit();
    expect(mockRouter.navigate).toHaveBeenCalledWith(['/sessions']);
  });

  test('should onInit if url includes "update", put onUpdate to true, call sessionApiService.detail, call initForm method and populate form with fetched session datas', () => {
    //@ts-ignore
    const initFormSpy = jest.spyOn(component, 'initForm');

    mockRouter.url = 'update';

    component.ngOnInit();
    expect(component.onUpdate).toBe(true);
    expect(mockSessionApiService.detail).toHaveBeenCalledWith('1');
    expect(initFormSpy).toHaveBeenCalledWith(mockSessionInformationData);
    expect(component.sessionForm?.get('name')?.value).toEqual(
      mockSessionInformationData.name
    );
  });

  test('should onInit, if url includes "create", have onUpdate to false, not call sessionApiService.detail, and initForm with empty values', () => {
    //@ts-ignore
    const initFormSpy = jest.spyOn(component, 'initForm');

    component.ngOnInit();
    expect(component.onUpdate).toBe(false);
    expect(mockSessionApiService.detail).not.toHaveBeenCalled();
    expect(initFormSpy).toHaveBeenCalledWith();
    expect(component.sessionForm?.get('name')?.value).toEqual('');
  });

  test('should on submit if not updating session, call sessionApiService.create with form values as session, and call exitPage method with message', () => {
    //@ts-ignore
    const exitPageSpy = jest.spyOn(component, 'exitPage');
    delete mockSessionInformationData.id;
    delete mockSessionInformationData.createdAt;
    delete mockSessionInformationData.updatedAt;
    delete mockSessionInformationData.users;
    component.sessionForm?.setValue(mockSessionInformationData);

    component.submit();
    expect(mockSessionApiService.create).toHaveBeenCalledWith(
      mockSessionInformationData
    );
    expect(mockMatSnackBar.open).toHaveBeenCalledWith(
      'Session created !',
      'Close',
      { duration: 3000 }
    );
    expect(exitPageSpy).toHaveBeenCalledWith('Session created !');
    expect(mockRouter.navigate).toHaveBeenCalledWith(['/sessions']);
  });

  test('should on submit if updating session, call sessionApiService.update, and call exitPage method with message', () => {
    //@ts-ignore
    const exitPageSpy = jest.spyOn(component, 'exitPage');

    mockRouter.url = 'update';
    component.ngOnInit();

    component.sessionForm?.setValue(mockUpdateSessionInformationData);

    component.submit();

    expect(mockSessionApiService.update).toHaveBeenCalledWith(
      '1',
      mockUpdateSessionInformationData
    );
    expect(mockMatSnackBar.open).toHaveBeenCalledWith(
      'Session updated !',
      'Close',
      { duration: 3000 }
    );
    expect(exitPageSpy).toHaveBeenCalledWith('Session updated !');
    expect(mockRouter.navigate).toHaveBeenCalledWith(['sessions']);
  });
});
