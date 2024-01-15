import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { SessionService } from 'src/app/services/session.service';

import { LoginComponent } from './login.component';
import { AuthService } from '../../services/auth.service';
import { of, throwError } from 'rxjs';
import { Router } from '@angular/router';

describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;
  let mockSessionService: any;
  let mockAuthService: any;
  let mockRouter: any;

  const mockSessionData = {
    token: 'randomTokenString',
    type: 'type',
    id: 101010,
    username: 'username',
    firstName: 'firstName',
    lastName: 'lastName',
    admin: false,
  };

  beforeEach(async () => {
    mockSessionService = { logIn: jest.fn() };
    mockAuthService = { login: jest.fn() };
    mockRouter = { navigate: jest.fn() };

    await TestBed.configureTestingModule({
      declarations: [LoginComponent],
      providers: [
        { provide: SessionService, useValue: mockSessionService },
        { provide: AuthService, useValue: mockAuthService },
        { provide: Router, useValue: mockRouter },
        FormBuilder,
      ],
      imports: [
        RouterTestingModule,
        BrowserAnimationsModule,
        HttpClientModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule,
      ],
    }).compileComponents();
    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  test('should create', () => {
    expect(component).toBeTruthy();
  });

  test('should create login form with initial values', () => {
    expect(component.form.get('email')?.value).toEqual('');
    expect(component.form.get('password')?.value).toEqual('');
  });

  test('should have required validator for email', () => {
    const email = component.form.get('email');
    expect(email?.valid).toBeFalsy();
    email?.setValue('test@test.com');
    expect(email?.valid).toBeTruthy();
  });

  test('should have min length and required validator for password', () => {
    const password = component.form.get('password');
    expect(password?.valid).toBeFalsy();
    password?.setValue('password');
    expect(password?.valid).toBeTruthy();
    password?.setValue('pa');
    expect(password?.valid).toBeFalsy();
  });

  test('should set onError to true on login error', () => {
    mockAuthService.login.mockReturnValue(throwError(() => new Error('error')));
    component.submit();
    expect(mockAuthService.login).toHaveBeenLastCalledWith({
      email: '',
      password: '',
    });
    expect(component.onError).toBe(true);
  });

  test('should call sessionService and redirect user on login success', () => {
    mockAuthService.login.mockReturnValue(of(mockSessionData));
    component.submit();
    expect(component.onError).toBe(false);
    expect(mockSessionService.logIn).toHaveBeenCalledWith(mockSessionData);
    expect(mockRouter.navigate).toHaveBeenCalledWith(['/sessions']);
  });
});
