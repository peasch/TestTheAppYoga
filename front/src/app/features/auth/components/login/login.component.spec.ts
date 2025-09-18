import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { SessionService } from 'src/app/services/session.service';

import { LoginComponent } from './login.component';
import {SessionInformation} from "../../../../interfaces/sessionInformation.interface";
import {AuthService} from "../../services/auth.service";
import {LoginRequest} from "../../interfaces/loginRequest.interface";
import {of, throwError} from "rxjs";

import {Router} from "@angular/router";

describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;
  let authService: AuthService;
  let router: Router;
  let sessionService: SessionService;
  let mockSessionInformation: SessionInformation = {
    token: 'token',
    type: 'mockType',
    id: 1,
    username: 'mockUserName',
    firstName: 'mockFirstName',
    lastName: 'mockLastName',
    admin: false,
  };
  let mockLoginRequest: LoginRequest = {
    email: 'bob@bob.fr',
    password: 'bobfr',
  };
  let mockLoginNoPwRequest: LoginRequest = {
    email: 'bob@bob.fr',
    password: '',
  };
  let routerSpy = {
    navigate: jest.fn().mockResolvedValue(true)
  };
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [LoginComponent],
      providers: [SessionService,
        { provide: Router, useValue: routerSpy }],
      imports: [
        RouterTestingModule,
        BrowserAnimationsModule,
        HttpClientModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule]
    })
      .compileComponents();
    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    authService = TestBed.inject(AuthService);
    sessionService = TestBed.inject(SessionService);
    router = TestBed.inject(Router);
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
  function updateForm(userEmail: string, userPassword: string) {
    component.form.controls['email'].setValue(userEmail);
    component.form.controls['password'].setValue(userPassword);
  }
  it('should call login et logIn methode', ()=>{
    //Given
    updateForm('bob@bob.fr','bobfr');
    let sessionServiceSpy = jest.spyOn(sessionService, 'logIn');
    let authServiceSpy = jest.spyOn(authService,'login').mockReturnValue(of(mockSessionInformation));

    //when
    component.submit();
    //then
    expect(authServiceSpy).toHaveBeenCalledWith(mockLoginRequest);
    expect(sessionServiceSpy).toHaveBeenCalled();
    expect(sessionService.isLogged).toBe(true);


  })

  it('should give an error if empty field',() =>{

    updateForm('','');

    const userEmail = component.form.controls.email;
    component.submit();

    expect(userEmail?.value).toBeFalsy();
    expect(userEmail?.valid).toBe(false);
  })
  it('should call login and throw login error', ()=>{
    //Given
    updateForm('bob@bob.fr','');
    let sessionServiceSpy = jest.spyOn(sessionService, 'logIn');
    let authServiceSpy = jest.spyOn(authService,'login').mockReturnValue(of(mockSessionInformation));
    //when
    component.submit();
    //then
    expect(authServiceSpy).toHaveBeenCalledWith(mockLoginNoPwRequest);

    expect(sessionServiceSpy).toThrowError();

  })
  it('should set onError to true when login fails', () => {

    updateForm('bob@bob.fr','');
    let sessionServiceSpy = jest.spyOn(sessionService, 'logIn');
    let authServiceSpy = jest.spyOn(authService,'login').mockImplementation(()=> throwError(() => new Error('err')));
    // Appelez la méthode submit
    component.submit();

    // Vérifiez que onError est défini à true

    expect(component.onError).toEqual(true);
    expect(authServiceSpy).toHaveBeenCalled();
    expect(sessionServiceSpy).not.toHaveBeenCalled();

  })
});
