import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { expect } from '@jest/globals';

import { RegisterComponent } from './register.component';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';
import { of, throwError } from 'rxjs';

describe('RegisterComponent', () => {
  let component: RegisterComponent;
  let fixture: ComponentFixture<RegisterComponent>;
  let mockAuthService: any;
  let mockRouter: any;

  let mockRegisterData = {
    email: 'test@test.com',
    firstName: 'someone',
    lastName: 'someone',
    password: 'password',
  };

  beforeEach(async () => {
    mockRouter = { navigate: jest.fn() };
    mockAuthService = { register: jest.fn() };

    await TestBed.configureTestingModule({
      declarations: [RegisterComponent],
      providers: [
        { provide: AuthService, useValue: mockAuthService },
        { provide: Router, useValue: mockRouter },
      ],
      imports: [
        BrowserAnimationsModule,
        HttpClientModule,
        ReactiveFormsModule,
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule,
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(RegisterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  test('should create', () => {
    expect(component).toBeTruthy();
  });

  test('should create register form wih initial values', () => {
    expect(component.form.get('email')?.value).toEqual('');
    expect(component.form.get('firstName')?.value).toEqual('');
    expect(component.form.get('lastName')?.value).toEqual('');
    expect(component.form.get('password')?.value).toEqual('');
  });

  test('should have required and validator.email on email form field', () => {
    const email = component.form.get('email');
    expect(email?.valid).toBeFalsy();
    email?.setValue('notAnEmail');
    expect(email?.valid).toBeFalsy();
    email?.setValue('test@test.com');
    expect(email?.valid).toBeTruthy();
  });

  test('should have required and min & max length on firstName form field', () => {
    const firstName = component.form.get('firstName');
    expect(firstName?.valid).toBeFalsy();
    firstName?.setValue('aa');
    expect(firstName?.valid).toBeFalsy();
    firstName?.setValue('aaaaaaaaaaaaaaaaaaaaaaaaa');
    expect(firstName?.valid).toBeFalsy();
    firstName?.setValue('Someone');
    expect(firstName?.valid).toBeTruthy();
  });

  test('should have required and min & max length on lastName form field', () => {
    const lastName = component.form.get('lastName');
    expect(lastName?.valid).toBeFalsy();
    lastName?.setValue('aa');
    expect(lastName?.valid).toBeFalsy();
    lastName?.setValue('aaaaaaaaaaaaaaaaaaaaaaaaa');
    expect(lastName?.valid).toBeFalsy();
    lastName?.setValue('Someone');
    expect(lastName?.valid).toBeTruthy();
  });

  test('should have required and min & max length on password form field', () => {
    const password = component.form.get('password');
    expect(password?.valid).toBeFalsy();
    password?.setValue('aa');
    expect(password?.valid).toBeFalsy();
    password?.setValue('aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa');
    expect(password?.valid).toBeFalsy();
    password?.setValue('Someone');
    expect(password?.valid).toBeTruthy();
  });

  test('should set onError to true on register error', () => {
    mockAuthService.register.mockReturnValue(
      throwError(() => new Error('error'))
    );
    expect(component.onError).toBe(false);
    component.submit();
    expect(component.onError).toBe(true);
  });

  test('should call authService.register with correct parameter and redirect user on register success', () => {
    mockAuthService.register.mockReturnValue(of(undefined));

    const email = component.form.get('email');
    const firstName = component.form.get('firstName');
    const lastName = component.form.get('lastName');
    const password = component.form.get('password');

    email?.setValue(mockRegisterData.email);
    firstName?.setValue(mockRegisterData.firstName);
    lastName?.setValue(mockRegisterData.lastName);
    password?.setValue(mockRegisterData.password);

    component.submit();

    expect(mockAuthService.register).toHaveBeenCalledWith(mockRegisterData);
    expect(component.onError).toBe(false);
    expect(mockRouter.navigate).toHaveBeenCalledWith(['/login']);
  });
});
