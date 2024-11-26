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
import {RegisterRequest} from "../../interfaces/registerRequest.interface";
import {AuthService} from "../../services/auth.service";
import {SessionInformation} from "../../../../interfaces/sessionInformation.interface";
import {of} from "rxjs";
import {SessionService} from "../../../../services/session.service";

describe('RegisterComponent', () => {
  let component: RegisterComponent;
  let fixture: ComponentFixture<RegisterComponent>;
  let authService: AuthService;
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

  let mockRegisterRequest: RegisterRequest ={
    email: "bob@bob.Fr",
    firstName: 'boby',
    lastName: 'Sixkiller',
    password: "bobyPassword!"
  }

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [RegisterComponent],
      imports: [
        BrowserAnimationsModule,
        HttpClientModule,
        ReactiveFormsModule,
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule
      ]
    })
      .compileComponents();

    fixture = TestBed.createComponent(RegisterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    authService =TestBed.inject(AuthService);
    sessionService = TestBed.inject(SessionService);
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
  function updateForm(userEmail: string, firstName: string,lastName: string, userPassword: string) {
    component.form.controls['email'].setValue(userEmail);
    component.form.controls['firstName'].setValue(firstName);
    component.form.controls['lastName'].setValue(lastName);
    component.form.controls['password'].setValue(userPassword);
  }

  it('should call register method',()=>{
    updateForm("bob@bob.Fr",'boby','Sixkiller', "bobyPassword!");

    let authServiceSpy = jest.spyOn(authService,'register');

    component.submit();

    expect(authServiceSpy).toHaveBeenCalledWith(mockRegisterRequest);


  })
  it('should give an error if empty field',() =>{

    updateForm('','','','');

    const userEmail = component.form.controls.email;
    component.submit();

    expect(userEmail?.value).toBeFalsy();
    expect(userEmail?.valid).toBe(false);

  })
  it('should give an error if invalid field',() =>{

    updateForm('useremail-fr','','','');

    const userEmail = component.form.controls.email;
    component.submit();

    expect(userEmail?.value).toBeDefined();
    expect(userEmail?.valid).toBe(false);


  })
  it('should be a valid field now ',() =>{
    updateForm('useremail@email.fr','','','');
    const userEmail = component.form.controls.email;

    component.submit();

    expect(userEmail?.value).toBeDefined();
    expect(userEmail?.valid).toBe(true);

  })
});
